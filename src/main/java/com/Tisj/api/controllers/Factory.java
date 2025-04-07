package com.Tisj.api.controllers;

import com.Tisj.api.Interfaces.ISystem;
import com.Tisj.bussines.entities.*;



public class Factory {

    public ISystem crearSystem() {
        return System.getInstance();
    }

    public Cliente crearCliente() {
        return new Cliente();
    }

    public ContentCreator crearContentCreator() {
        return new ContentCreator();
    }

    public Admin crearAdmin() {
        return new Admin();
    }

    public Carrito crearCarrito() {
        return new Carrito();
    }

    public Pago CrearPago() {
        return new Pago();
    }
}

