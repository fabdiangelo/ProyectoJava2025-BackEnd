package com.Tisj.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class ArtCliResponse {
    private Long id;
    private LocalDate caducidad;
    private Boolean completado;
    private String usrEmail;
    private String usrNombre;
    private Long artId;
    private String artNombre;
}
