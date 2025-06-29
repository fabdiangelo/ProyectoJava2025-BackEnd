/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Tisj.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@EnableMethodSecurity(prePostEnabled = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .addFilterBefore(new FiltroJWTAutorizacion(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth

                        // Rutas públicas (no requieren autenticación ni token)
                        .requestMatchers(antMatcher("/api/seguridad/**")).permitAll()
                        .requestMatchers(antMatcher("/v3/api-docs/**")).permitAll() // Documentación Swagger
                        .requestMatchers(antMatcher("/swagger-ui/**")).permitAll() // UI de Swagger
                        .requestMatchers(antMatcher("/swagger-resources/**")).permitAll()
                        .requestMatchers(antMatcher("/configuration/**")).permitAll()
                        .requestMatchers(antMatcher(HttpMethod.GET, "/api/curso")).permitAll() // Listar cursos (público)
                        .requestMatchers(antMatcher(HttpMethod.POST, "/api/mercado-pago/webhook")).permitAll() // Webhook de Mercado Pago (público)
                        .requestMatchers(antMatcher("/api/paypal/**")).hasAnyAuthority("USER", "ADMIN") // Endpoints de PayPal protegidos
                        .requestMatchers(antMatcher(HttpMethod.POST, "/api/usuarios")).permitAll() // Registro público
                        .requestMatchers(antMatcher("/api/usuarios/**")).hasAnyAuthority("USER", "ADMIN") // Gestión de usuario logueado
                        .requestMatchers(antMatcher(HttpMethod.PUT, "/api/articulos/{id}")).hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(antMatcher(HttpMethod.DELETE, "/api/articulos/{id}")).hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(antMatcher("/api/articulos/**")).hasAuthority("ADMIN")
                        .requestMatchers(antMatcher(HttpMethod.GET,"/api/paquetes/**")).permitAll() // Endpoints de paquete
                        .requestMatchers(antMatcher(HttpMethod.GET,"/api/paquetes")).permitAll() // Endpoints de paquete
                        .requestMatchers(antMatcher("/api/articulos_cliente/usuario/**")).hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(antMatcher(HttpMethod.PUT, "/api/articulos_cliente/completar/{id}")).hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(antMatcher(HttpMethod.PUT, "/api/articulos_cliente/reiniciar/{id}")).hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(antMatcher("/api/articulos_cliente/**")).hasAuthority("ADMIN") // CRUD completo para ADMIN
                        .requestMatchers(antMatcher(HttpMethod.GET, "/api/carrito/me")).hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(antMatcher("/api/carrito/**")).hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(antMatcher("/api/pedidos/**")).hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(antMatcher("/api/pago/**")).hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(antMatcher("/api/orders/**")).hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(antMatcher("/api/oferta/**")).hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(antMatcher("/api/video/**")).hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(antMatcher("/api/youtube/**")).hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(antMatcher("/api/email/**")).hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(antMatcher(HttpMethod.POST, "/api/mercado-pago/crear-preferencia")).hasAnyAuthority("USER", "ADMIN") // Webhook de Mercado Pago (público)

                        // Cualquier otra petición requiere autenticación (esta regla actúa como "catch-all" para APIs no listadas específicamente, pero las anteriores tienen prioridad)
                        .anyRequest()
                        .authenticated());

        return http.build();
    }

    @Bean
    public WebMvcConfigurer configurarCorsGlobal(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry){
                registry.addMapping("/api/**")
                        // .allowCredentials(true) //solo si usan cookies
                        .allowedMethods("GET","POST", "PUT", "DELETE")
                        .allowedHeaders("*")
                        .allowedOriginPatterns(
                            "http://localhost:*",                         // cualquier puerto en localhost
                            "https://solfuentes-prueba.netlify.app"      // frontend en producción
                );
            }
        };
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("http://localhost:*");
        configuration.addAllowedOrigin("https://solfuentes-prueba.netlify.app");
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("*"));
        // configuration.setAllowCredentials(true); // solo si usás cookies o Authorization

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
