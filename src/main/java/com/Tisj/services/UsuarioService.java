package com.Tisj.services;

import com.Tisj.api.response.ListadoUsuarios;
import com.Tisj.bussines.entities.Cliente;
import com.Tisj.bussines.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    public final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public ListadoUsuarios listadoPersonas(){
        ListadoUsuarios listadoUsuarios = new ListadoUsuarios();
        listadoUsuarios.setUsuarios(usuarioRepository.findAll());
        return listadoUsuarios;
    }

    public String crearCliente(Cliente cliente){
        String response = null;
        if(cliente != null && usuarioRepository.findById(cliente.getEmail()).isEmpty()){
            response = "Cliente creada: " + usuarioRepository.save(cliente).getEmail();
        }
        return response;
    }
}
