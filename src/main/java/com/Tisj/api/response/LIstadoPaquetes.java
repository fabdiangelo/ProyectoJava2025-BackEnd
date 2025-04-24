package com.Tisj.api.response;

import com.Tisj.bussines.entities.Paquete;

import lombok.Data;

import java.util.List;

@Data
public class LIstadoPaquetes {
    private List<Paquete> usuarios;
}
