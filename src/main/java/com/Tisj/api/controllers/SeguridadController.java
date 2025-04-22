package com.Tisj.api.controllers;

import com.Tisj.api.response.TokenUsuario;
import com.Tisj.bussines.entities.Usuario;
import com.Tisj.security.SeguridadService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.System;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("api/v1/seguridad")
public class SeguridadController {

    
    @Autowired
    private SeguridadService seguridadService;

    @PostMapping("/autenticacion")
    @Transactional(readOnly = true)
    public ResponseEntity<TokenUsuario> autenticarUsuario(
            @RequestParam("email") String email,
            @RequestParam("password") String password
    ) {
        Usuario objUsuario = seguridadService
                .autenticarUsuario(email, password)
                .orElseThrow(() -> new RuntimeException("Usuario o password incorrecto."));
        String token = generarToken(objUsuario);
        TokenUsuario usuarioResponse
                = new TokenUsuario(objUsuario.getNombre(), objUsuario.getEmail(),
                        token, seguridadService.listarRolesPorUsuario(objUsuario));
        return new ResponseEntity<>(usuarioResponse, HttpStatus.OK);
    }

    private String generarToken(Usuario usuario) {
        String clave = "@TI2025"; // dinamico desde la BD
        List<GrantedAuthority> grantedAuthorityList
                = AuthorityUtils.createAuthorityList(
                        seguridadService.listarRolesPorUsuario(usuario)
                );
        String token = Jwts
                .builder()
                .setId("@acchsjwt") // Dinámico desde BD
                .setSubject(usuario.getEmail())
                .claim("authorities",
                        grantedAuthorityList.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList())
                )
                .setIssuedAt(new Date(java.lang.System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 8)))
                .signWith(SignatureAlgorithm.HS512, clave.getBytes())
                .compact();
        return token;
    }

}
