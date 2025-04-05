package com.Tisj.bussines.entities.DT;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DTFactura {
    private Integer id;
    private String nombreUsuario;
    private String correo;
//    private List<Articulo> items;
    private LocalDate fecha;
    private Float monto;
}
