package com.Tisj.security;


import com.Tisj.bussines.entities.Usuario;
import com.Tisj.bussines.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class SeguridadService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Optional<Usuario> autenticarUsuario(String email,
                                               String password) {
        Optional<Usuario> objUsuario = usuarioRepository.findByEmail(email);

        if (objUsuario.isEmpty()) {
            return Optional.empty();
        } else if (!objUsuario.get().getActivo()) {
            return Optional.empty();
        }else if(!passwordEncoder.matches(password, objUsuario.get().getPassword())){
            return Optional.empty();
        }
        return objUsuario;
    }

    public String[] listarRolesPorUsuario(Usuario usuario) {
        String[] lisRoles = new String[usuario.getRoles().size()];
        for (int i = 0; i < usuario.getRoles().size(); i++) {
            lisRoles[i] = usuario.getRoles().get(i).getNombre();
        }
        return lisRoles;
    }

}
