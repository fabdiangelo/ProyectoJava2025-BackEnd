package com.Tisj.api.response;

import com.Tisj.bussines.entities.DT.DTUsuario;
import lombok.Data;

import java.util.List;

@Data
public class ListadoUsuarios {
    private List<DTUsuario> usuarios;
}
