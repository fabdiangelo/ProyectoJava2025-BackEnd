package com.Tisj.api.requests;

import com.Tisj.bussines.entities.Enum.EnumDescuento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RequestOferta {
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 10, max = 500, message = "La descripción debe tener entre 10 y 500 caracteres")
    private String descripcion;

    @NotNull(message = "El valor del descuento es obligatorio")
    private EnumDescuento valor;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate inicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDate fin;

    private boolean activo = true;
}
