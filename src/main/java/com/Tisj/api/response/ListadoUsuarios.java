package com.Tisj.api.response;

import com.Tisj.bussines.entities.DT.DTUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ListadoUsuarios {
    private List<DTUsuario> usuarios;
}
