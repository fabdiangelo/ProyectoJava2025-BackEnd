package com.Tisj.security;

import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class FiltroJWTAutorizacion extends OncePerRequestFilter {

    private final String CLAVE = "@TI2025";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            if(varlidarUsoDeToken(request, response)){
                Claims claims = validarToken(request);
                if(claims.get("authorities") != null){
                    crearAutenticacion(claims);
                }else {
                    SecurityContextHolder.clearContext();
                }
            }else{
                SecurityContextHolder.clearContext();
            }
            filterChain.doFilter(request, response);
        }catch (ExpiredJwtException |
                UnsupportedJwtException |
                MalformedJwtException ex
        ){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            ((HttpServletResponse)response).sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    ex.getMessage()
            );
        }
    }

    private void crearAutenticacion(Claims claims){
        List<String> autorizaciones = (List<String>)claims.get("authorities");
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(
                        claims.getSubject(),
                null,
                autorizaciones.stream().map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList())
        );
        SecurityContextHolder.getContext().setAuthentication(
                authenticationToken
        );
    }

    private Claims validarToken(HttpServletRequest request){
        String tokenCliente = request.getHeader("Authorization")
                .replace("Bearer ", "");
        return Jwts.parser().setSigningKey(CLAVE.getBytes())
                .parseClaimsJws(tokenCliente).getBody();
    }


    private boolean varlidarUsoDeToken(HttpServletRequest request,
                                       HttpServletResponse response){
        String autenticacion = request.getHeader("Authorization");
        if(autenticacion == null || !autenticacion.startsWith("Bearer ")){
            return false;
        }
        return true;
    }

}
