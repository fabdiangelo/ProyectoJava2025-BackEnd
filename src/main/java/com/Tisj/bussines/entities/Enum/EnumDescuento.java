package com.Tisj.bussines.entities.Enum;

public enum EnumDescuento {
    VEINTE(0.2f),
    TREINTA(0.3f),
    CINCUENTA(0.5f),
    OCHENTA(0.8f),
    NOVENTA(0.9f);

    private final float value;

    EnumDescuento(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }
}
