package com.Tisj.bussines.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Cliente extends Usuario{
//    private List<EstadoCompletado> estadosCursos;
//    private List<CursoCliente> cursos;
//    private List<Reserva> reservas;
    @OneToOne
    private Carrito carrito;
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.REFRESH, orphanRemoval = true)
    private List<Pago> pagos;

    public Void realizarPago(){
        return null;
    }
    public Void agregarAlCarrito(){
        return null;
    }
//    public Curso verCurso(){
//        return null;
//    }
//
//    public Video verVideo(){
//        return null;
//    }
//    public Carrito crearCarrito(){
//        return null;
//    }
    public Void modificarCarrito(){
        return null;
    }
    public Void eliminarCarrito(){
        return null;
    }
}
