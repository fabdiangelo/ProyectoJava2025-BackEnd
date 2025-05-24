package com.Tisj.api.requests;

import com.Tisj.bussines.entities.Video;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RequestCurso {
    private String nombre;
    private String descripcion;
    private Float precio;
    private String videoPresentacion;
    private List<RequestVideo> videos;
}
