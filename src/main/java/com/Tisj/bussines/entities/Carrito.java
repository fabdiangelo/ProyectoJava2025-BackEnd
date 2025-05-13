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
    private Long id;

    private LocalDate vencimiento;
    private boolean activo = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "carrito-articulo",
            joinColumns = @JoinColumn(name = "carrito_id"),
            inverseJoinColumns = @JoinColumn(name = "articulo_id")
    )
    private List<Articulo> items;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pago_id")
    private Pago pago;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public Carrito(){
        this.vencimiento = LocalDate.now().plusWeeks(1);
        this.items = new ArrayList<>();
        this.pago = null;
        this.activo = true;
    }

    public Boolean quitarItem(int id){
        if (!this.activo) return false;
        return items.removeIf(item -> item.getId().equals(id));
    }

    public Boolean quitarElementoDelCarrito(Long articuloId) {
        return quitarItem(articuloId.intValue());
    }

    public Articulo agregarAlCarrito(Articulo articulo) {
        if (!this.activo) return null;
        if (articulo == null || !articulo.isActivo()) return null;
        if (items.add(articulo)) {
            return articulo;
        }
        return null;
    }
    

    public Float getMontoTotal(){
        if (!this.activo) return 0f;
        return items.stream()
                .map(Articulo::getPrecio)
                .reduce(0f, Float::sum);
    }

    public Float pedirMonto() {
        return getMontoTotal();
    }

    public void desactivar() {
        this.activo = false;
    }

    public void activar() {
        this.activo = true;
    }
}
