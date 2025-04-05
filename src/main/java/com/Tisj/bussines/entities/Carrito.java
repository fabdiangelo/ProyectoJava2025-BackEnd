package com.Tisj.bussines.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
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
