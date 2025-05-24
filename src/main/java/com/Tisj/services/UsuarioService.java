package com.Tisj.services;

import com.Tisj.api.requests.RequestUsuario;
import com.Tisj.api.response.ListadoUsuarios;
import com.Tisj.bussines.entities.DT.DTUsuario;
import com.Tisj.bussines.entities.RolUsuario;
import com.Tisj.bussines.entities.Usuario;
import com.Tisj.bussines.repositories.RolUsuarioRepository;
import com.Tisj.bussines.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    public final UsuarioRepository usuarioRepository;
    public final RolUsuarioRepository rolUsuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, RolUsuarioRepository rolUsuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolUsuarioRepository = rolUsuarioRepository;
    }

    public ListadoUsuarios listadoPersonas(){
        List<DTUsuario> list = usuarioRepository.findAll()
                    .stream()
                    .map(Usuario::crearDT)
                    .toList();
        return new ListadoUsuarios(list);
    }

    public String crearCliente(RequestUsuario requestUsuario){
        Usuario user = requestUsuarioToUsuario(requestUsuario);
        String response = null;
        if(user != null && usuarioRepository.findById(user.getEmail()).isEmpty()){
            Optional<RolUsuario> rol = rolUsuarioRepository.findByNombre("USER");
            if(rol.isPresent()){
                String encodedPassword = passwordEncoder.encode(user.getPassword());
                user.setPassword(encodedPassword);
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

    public String actualizarUsuario(RequestUsuario changes) {
        String response = null;
        Usuario user = usuarioRepository.findById(changes.getEmail()).orElse(null);
        if(user != null && changes != null){
            user.setPassword(changes.getPassword());
            user.setNombre(changes.getNombre());
            user.setApellido(changes.getApellido());
            user.setActivo(changes.getActivo());
            response = "Cliente modificado: " + usuarioRepository.save(user).getEmail();
        }
        return response;
    }

    @Transactional
    public String eliminarUsuario(String email) {
        String response = null;
        if(email != null){
            Optional<Usuario> user = usuarioRepository.findById(email);
            if(user.isPresent()){
                user.get().setActivo(false);
                //usuarioRepository.deleteByEmail(email);
                response = "Cliente eliminado: " + usuarioRepository.save(user.get()).getEmail();
            }
        }
        return response;
    }

    public Usuario requestUsuarioToUsuario(RequestUsuario req){
        return new Usuario(
                req.getEmail(),
                req.getPassword(),
                req.getNombre(),
                req.getApellido()
        );
    }
}



