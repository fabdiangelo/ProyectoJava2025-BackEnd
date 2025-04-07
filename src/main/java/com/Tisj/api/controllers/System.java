package com.Tisj.api.controllers;


import com.Tisj.api.Interfaces.ISystem;
import com.Tisj.bussines.entities.*;
import java.util.List;

public class System implements ISystem {

    private static System instance; // Instancia única (Singleton)

    private System() {
        // Constructor privado para evitar la creación de instancias desde fuera
    }

    public static System getInstance() {
        if (instance == null) {
            instance = new System();
        }
        return instance;
    }


    @Override
    public Usuario RegistrarUsuario() {
        return null;
    }

    @Override
    public void ModificarUsuario() {

    }

    @Override
    public void EliminarUsuario() {

    }

    @Override
    public void ModificarCurso() {

    }

    @Override
    public void EliminarCurso() {

    }

    @Override
    public void ModificarPaquete() {

    }

    @Override
    public void EliminarPaquete() {

    }

    @Override
    public List<Usuario> ListarUsuario() {
        return List.of();
    }

    @Override
    public Pago CrearPago() {
        return null;
    }

    @Override
    public System crearSystem() {
        return null;
    }

    @Override
    public Carrito crearCarrito() {
        return null;
    }


}
