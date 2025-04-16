package com.Tisj.services;

import com.Tisj.api.requests.ModificableUsuario;
import com.Tisj.api.response.ListadoUsuarios;
import com.Tisj.bussines.entities.DT.DTUsuario;
import com.Tisj.bussines.entities.RolUsuario;
import com.Tisj.bussines.entities.Usuario;
import com.Tisj.bussines.repositories.RolUsuarioRepository;
import com.Tisj.bussines.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        listadoUsuarios.setUsuarios(
                usuarioRepository.findAll()
                    .stream()
                    .map(Usuario::crearDT)
                    .collect(Collectors.toList()));
        return listadoUsuarios;
    }

    public String crearCliente(Usuario user){
        String response = null;
        if(user != null && usuarioRepository.findById(user.getEmail()).isEmpty()){
            Optional<RolUsuario> rol = rolUsuarioRepository.findByNombre("USER");
            if(rol.isPresent()){
                agregarRolUsuario(user, rol.get());
                response = "Cliente creado: " + usuarioRepository.save(user).getEmail();
            }
        }
        return response;
    }

    public void agregarRolUsuario(Usuario user, RolUsuario rol){
        List<RolUsuario> roles = user.getRoles();
        roles.add(rol);
        user.setRoles(roles);
    }


    public DTUsuario getUsuario(String email) {
        Optional<Usuario> user = usuarioRepository.findById(email);
        return user.map(Usuario::crearDT).orElse(null);
    }

    public String actualizarUsuario(ModificableUsuario changes) {
        String response = null;
        Usuario user = usuarioRepository.findById(changes.getEmail()).orElse(null);
        if(user != null && changes != null){
            user.setNombre(changes.getNombre());
            user.setApellido(changes.getApellido());
            user.setPassword(changes.getPassword());
            user.setNacimiento(changes.getNacimiento());
            user.setGenero(changes.getGenero());
            response = "Cliente modificado: " + usuarioRepository.save(user).getEmail();
        }
        return response;
    }

    public String eliminarUsuario(String email) {
        String response = null;
        if(email != null && usuarioRepository.findById(email).isPresent()){
            usuarioRepository.deleteByEmail(email);
            response = "Cliente eliminado: " + email;
        }
        return response;
    }
}



