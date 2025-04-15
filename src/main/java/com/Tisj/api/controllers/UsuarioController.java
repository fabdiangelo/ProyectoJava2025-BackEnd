package com.Tisj.api.controllers;

import com.Tisj.api.response.ListadoUsuarios;
import com.Tisj.bussines.entities.Usuario;
import com.Tisj.services.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.lang.System;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ListadoUsuarios> getPersonas(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

         auth.getAuthorities().stream().forEach((e) -> System.out.println(e));
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))
        ) {
            ListadoUsuarios response = usuarioService.listadoPersonas();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
    }

//    @PostMapping("/admin")
//    public ResponseEntity<String> createAdmin(@RequestBody Usuario admin){
//        String response = usuarioService.crearAdmin(admin);
//        if(response == null){
//            return new ResponseEntity<>("Error al crear a la persona", HttpStatus.BAD_REQUEST);
//        }else{
//            return  new ResponseEntity<>(response, HttpStatus.CREATED);
//        }
//    }

    @PostMapping("/user")
    public ResponseEntity<String> createUser(@RequestBody Usuario cliente){
        String response = usuarioService.crearCliente(cliente);
        if(response == null){
            return new ResponseEntity<>("Error al crear a la persona", HttpStatus.BAD_REQUEST);
        }else{
            return  new ResponseEntity<>(response, HttpStatus.CREATED);
        }
    }
}
