package com.Tisj.api.response;

import com.Tisj.bussines.entities.Oferta;
import lombok.Data;

import java.util.List;

@Data
public class ListadoOfertas {
    private List<Oferta> usuarios;
}
