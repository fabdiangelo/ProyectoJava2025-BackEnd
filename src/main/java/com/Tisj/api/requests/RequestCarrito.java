package com.Tisj.api.requests;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestCarrito {
    private List<Long> itemIds;
    private Long pagoId;
}
