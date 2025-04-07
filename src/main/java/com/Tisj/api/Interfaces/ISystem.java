package com.Tisj.api.Interfaces;

import com.Tisj.api.controllers.System;
import com.Tisj.bussines.entities.*;

import java.util.List;

public interface ISystem {

    
    Usuario RegistrarUsuario();
    void ModificarUsuario();
    void EliminarUsuario();
    void ModificarCurso();
    void EliminarCurso();
    void ModificarPaquete();
    void EliminarPaquete();
    List<Usuario> ListarUsuario();
    Pago CrearPago();
    System crearSystem();
    Carrito crearCarrito();
//    Paquete CrearPaquete();
//    Curso CrearCurso();
//    List<Curso> ListarCursos();
//    List<Paquete> ListarPaquete();
//    Video crearVideo();
//    Articulo crearArticulo();
//    CursoCliente CrearCursoCliente();
//    EstadoCliente CrearEstadoCliente();





}
