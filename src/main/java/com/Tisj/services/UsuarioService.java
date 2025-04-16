package com.Tisj.services;

import com.Tisj.api.response.ListadoUsuarios;
import com.Tisj.bussines.entities.RolUsuario;
import com.Tisj.bussines.entities.Usuario;
import com.Tisj.bussines.repositories.RolUsuarioRepository;
import com.Tisj.bussines.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    public final UsuarioRepository usuarioRepository;
    public final RolUsuarioRepository rolUsuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, RolUsuarioRepository rolUsuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolUsuarioRepository = rolUsuarioRepository;
    }

    public ListadoUsuarios listadoPersonas(){
        ListadoUsuarios listadoUsuarios = new ListadoUsuarios();
        listadoUsuarios.setUsuarios(usuarioRepository.findAll());
        return listadoUsuarios;
    }

    public String crearCliente(Usuario cliente){
        String response = null;
        if(cliente != null && usuarioRepository.findById(cliente.getEmail()).isEmpty()){
            Optional<RolUsuario> rol = rolUsuarioRepository.findByNombre("USER");
            if(rol.isPresent()){
                agregarRolUsuario(cliente, rol.get());
                response = "Cliente creado: " + usuarioRepository.save(cliente).getEmail();
            }
        }
        return response;
    }

    public String crearAdmin(Usuario admin) {
        String response = null;
        if(admin != null && usuarioRepository.findById(admin.getEmail()).isEmpty()){
            Optional<RolUsuario> rol = rolUsuarioRepository.findByNombre("ADMIN");
            if(rol.isPresent()){
                agregarRolUsuario(admin, rol.get());
                response = "Admin creado: " + usuarioRepository.save(admin).getEmail();
            }
        }
        return response;
    }

    public void agregarRolUsuario(Usuario user, RolUsuario rol){
        List<RolUsuario> roles = user.getRoles();
        roles.add(rol);
        user.setRoles(roles);
    }


}



