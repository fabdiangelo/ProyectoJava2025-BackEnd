package com.Tisj.api.controllers;

import com.Tisj.api.Interfaces.ISystem;
import com.Tisj.bussines.entities.*;



public class Factory {

    public static ISystem crearSystem() {
        return System.getInstance();
    }

    public static Carrito crearCarrito() {
        return new Carrito();
    }

    public static Pago CrearPago() {
        return new Pago();
    }
}

