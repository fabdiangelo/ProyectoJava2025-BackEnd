package com.Tisj.api.requests;

import com.Tisj.bussines.entities.DT.DTArtCarrito;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RequestMP {
    private List<DTArtCarrito> items;
    private Long usuarioId;
    private Long carritoId;
}
