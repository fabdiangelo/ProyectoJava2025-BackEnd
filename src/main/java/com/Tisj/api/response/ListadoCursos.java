package com.Tisj.api.response;

import com.Tisj.bussines.entities.Curso;
import lombok.Data;

import java.util.List;

@Data
public class ListadoCursos {
    private List<Curso> usuarios;
}
