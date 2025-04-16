package com.Tisj.bussines.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "carrito")
public class Carrito {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
//    private List<Articulo> items;

    public Boolean quitarItem(Integer id){
        return false;
    }
    public Boolean agregarItem(Integer id){
        return false;
    }
    public Float getMontoTotal(){
        return (float) 0;
    }
}
