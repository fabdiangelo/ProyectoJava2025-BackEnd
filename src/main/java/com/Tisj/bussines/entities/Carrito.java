package com.Tisj.bussines.entities;

import jakarta.persistence.*;

import lombok.Getter;

import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Carrito {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private LocalDate vencimiento;

    @ManyToMany
    @JoinTable(
            name = "carrito-articulo",
            joinColumns = @JoinColumn(name = "carrito_id"),
            inverseJoinColumns = @JoinColumn(name = "articulo_id")
    )
    private List<Articulo> items;

    @OneToOne
    @JoinColumn(name = "pago_id")
    private Pago pago;

    public Carrito(){
        this.vencimiento = LocalDate.now().plusWeeks(1);
        this.items = new ArrayList<>();
        this.pago = null;
    }

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
