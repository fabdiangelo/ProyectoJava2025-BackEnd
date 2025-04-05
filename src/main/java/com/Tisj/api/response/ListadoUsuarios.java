package com.Tisj.api.response;

import com.Tisj.bussines.entities.Usuario;
import lombok.Data;

import java.util.List;

@Data
public class ListadoUsuarios {
    private List<Usuario> usuarios;
}
