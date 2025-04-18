package com.Tisj.api.controllers;

import com.Tisj.api.requests.ModificableUsuario;
import com.Tisj.api.response.ListadoUsuarios;
import com.Tisj.bussines.entities.DT.DTUsuario;
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

    @GetMapping
    public ResponseEntity<ListadoUsuarios> getListaUsuarios(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))
        ) {
            ListadoUsuarios response = usuarioService.listadoPersonas();
            if(response == null) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<DTUsuario> getUsuario(@PathVariable(name = "email") String email) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))
        ) {
            DTUsuario response = usuarioService.getUsuario(email);
            if(response == null) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping()
    public ResponseEntity<String> createUser(@RequestBody Usuario user){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))
        ) {
            String response = usuarioService.crearCliente(user);
            if(response == null){
                return new ResponseEntity<>("Error al crear el usuario", HttpStatus.BAD_REQUEST);
            }else{
                return  new ResponseEntity<>(response, HttpStatus.CREATED);
            }
        }else{
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping()
    public ResponseEntity<String> updateUser(@RequestBody ModificableUsuario user){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))
        ) {
            String response = usuarioService.actualizarUsuario(user);
            if(response == null){
                return new ResponseEntity<>("Error al modificar el usuario", HttpStatus.BAD_REQUEST);
            }else{
                return  new ResponseEntity<>(response, HttpStatus.OK);
            }
        }else{
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }

    }

    @DeleteMapping("/{email}")
    public ResponseEntity<String> deleteUser(@PathVariable(name = "email") String email){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))
        ) {
            String response = usuarioService.eliminarUsuario(email);
            if(response == null){
                return new ResponseEntity<>("Error al eliminar el usuario", HttpStatus.BAD_REQUEST);
            }else{
                return  new ResponseEntity<>(response, HttpStatus.OK);
            }
        }else {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
    }
}
