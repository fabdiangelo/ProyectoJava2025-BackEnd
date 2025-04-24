package com.Tisj.api.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestPago {
    private Long usuarioId;
    private Float monto;
}
