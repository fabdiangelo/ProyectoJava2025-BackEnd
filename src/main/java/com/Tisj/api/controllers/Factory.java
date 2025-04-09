package com.Tisj.api.controllers;

import com.Tisj.api.Interfaces.ISystem;
import com.Tisj.bussines.entities.*;



public class Factory {

    public static ISystem crearSystem() {
        return System.getInstance();
    }

    public static Cliente crearCliente() {
        return new Cliente();
    }

    public static ContentCreator crearContentCreator() {
        return new ContentCreator();
    }

    public static Admin crearAdmin() {
        return new Admin();
    }

    public static Carrito crearCarrito() {
        return new Carrito();
    }

    public static Pago CrearPago() {
        return new Pago();
    }
}

