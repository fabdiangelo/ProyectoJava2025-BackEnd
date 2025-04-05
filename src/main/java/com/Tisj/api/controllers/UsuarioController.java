package com.Tisj.api.controllers;

import com.Tisj.api.response.ListadoUsuarios;
import com.Tisj.bussines.entities.Cliente;
import com.Tisj.services.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<ListadoUsuarios> getPersonas(){
        ListadoUsuarios response = usuarioService.listadoPersonas();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createCliente(@RequestBody Cliente cliente){
        String response = usuarioService.crearCliente(cliente);
        if(response == null){
            return new ResponseEntity<>("Error al crear a la persona", HttpStatus.BAD_REQUEST);
        }else{
            return  new ResponseEntity<>(response, HttpStatus.CREATED);
        }
    }
}
