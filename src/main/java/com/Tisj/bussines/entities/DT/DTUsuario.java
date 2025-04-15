package com.Tisj.bussines.entities.DT;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DTUsuario {

    private String nombreUsuario;
    private String token;
    private String[] roles;

}
