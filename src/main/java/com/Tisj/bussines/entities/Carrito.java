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
    private static final int MAX_ITEMS_PER_CART = 50; // Límite máximo de items por carrito

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

    public Boolean quitarItem(Long id) {
        if (!this.activo) {
            throw new IllegalStateException("El carrito no está activo");
        }
        return items.removeIf(item -> item.getId().equals(id));
    }

    public Boolean quitarElementoDelCarrito(Long articuloId) {
        if (articuloId == null) {
            throw new IllegalArgumentException("El ID del artículo no puede ser nulo");
        }
        return quitarItem(articuloId);
    }

    public Articulo agregarAlCarrito(Articulo articulo) {
        if (!this.activo) {
            throw new IllegalStateException("El carrito no está activo");
        }
        if (articulo == null) {
            throw new IllegalArgumentException("El artículo no puede ser nulo");
        }
        if (!articulo.isActivo()) {
            throw new IllegalStateException("El artículo no está disponible");
        }
        if (items.contains(articulo)) {
            throw new IllegalStateException("El artículo ya está en el carrito");
        }
        if (items.size() >= MAX_ITEMS_PER_CART) {
            throw new IllegalStateException("El carrito ha alcanzado el límite de items");
        }
        
        if (items.add(articulo)) {
            return articulo;
        }
        return null;
    }
    public String getUsuarioId(){
        return this.usuario != null ? this.usuario.getEmail() : null;
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

    public boolean isVencido() {
        return LocalDate.now().isAfter(this.vencimiento);
    }

    public void renovarVencimiento() {
        this.vencimiento = LocalDate.now().plusWeeks(1);
    }

    public void desactivar() {
        this.activo = false;
    }

    public void activar() {
        this.activo = true;
    }
}
