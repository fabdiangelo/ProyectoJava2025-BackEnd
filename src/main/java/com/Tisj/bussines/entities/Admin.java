package com.Tisj.bussines.entities;

import jakarta.persistence.Entity;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Admin extends Usuario{

    public Cliente crearCliente(){
        return new Cliente();
    }
    public Cliente modificarCliente(){
        return null;
    }
    public List<Cliente> listarClientes(){
        return null;
    }
//    public Video verVideo(){
//        return null;
//    }
    public Void crearContentCreator(){
        return null;
    }
    public Void modificarContenCreator(){
        return null;
    }
    public Void eliminarContenCreator(){
        return null;
    }
    public Void eliminarVideo(){
        return null;
    }
    public Void modificarVideo(){
        return null;
    }
    public Void agregarVideo(){
        return null;
    }
}
