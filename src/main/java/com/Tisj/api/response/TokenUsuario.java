package com.Tisj.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TokenUsuario {
    private String nombre, email, token;
    private String[] roles;
}
