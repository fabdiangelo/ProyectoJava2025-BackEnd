package com.Tisj.services;

import com.Tisj.bussines.entities.Carrito;
import com.Tisj.bussines.entities.Articulo;
import com.Tisj.bussines.repositories.CarritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ArticuloService articuloService;

    public Carrito createCarrito(Carrito carrito) {
        carrito.setActivo(true);
        return carritoRepository.save(carrito);
    }

    @Transactional(readOnly = true)
    public Carrito getCarritoById(Long id) {
        Carrito carrito = carritoRepository.findById(id)
                .filter(Carrito::isActivo)
                .orElse(null);
        if (carrito != null) {
            // Inicializar la colección items
            carrito.getItems().size();
        }
        return carrito;
    }

    @Transactional(readOnly = true)
    public List<Carrito> getAllCarritos() {
        List<Carrito> carritos = carritoRepository.findAll().stream()
                .filter(Carrito::isActivo)
                .toList();
        // Inicializar las colecciones items
        carritos.forEach(carrito -> carrito.getItems().size());
        return carritos;
    }

    public void deleteCarrito(Long id) {
        Carrito carrito = getCarritoById(id);
        if (carrito != null) {
            carrito.setActivo(false);
            carritoRepository.save(carrito);
        }
    }

    @Transactional(readOnly = true)
    public Carrito getCarritoByUsuarioEmail(String email) {
        Carrito carrito = carritoRepository.findByUsuarioEmail(email)
                .filter(Carrito::isActivo)
                .orElse(null);
        if (carrito != null) {
            // Inicializar la colección items
            carrito.getItems().size();
        }
        return carrito;
    }

    @Transactional
    public Carrito agregarItemAlCarrito(Long carritoId, Long articuloId) {
        Carrito carrito = getCarritoById(carritoId);
        Articulo articulo = articuloService.getArticuloById(articuloId);
        if (carrito != null && articulo != null && carrito.isActivo()) {
            Articulo agregado = carrito.agregarAlCarrito(articulo);
            if (agregado != null) {
                return carritoRepository.save(carrito);
            }
        }
        return null;
    }

    @Transactional
    public Carrito quitarItemDelCarrito(Long carritoId, Long articuloId) {
        Carrito carrito = getCarritoById(carritoId);
        if (carrito != null && carrito.isActivo()) {
            Boolean quitado = carrito.quitarElementoDelCarrito(articuloId);
            if (quitado) {
                return carritoRepository.save(carrito);
            }
        }
        return null;
    }

    @Transactional
    public Carrito desactivarCarrito(Long carritoId) {
        Carrito carrito = getCarritoById(carritoId);
        if (carrito != null) {
            carrito.desactivar();
            return carritoRepository.save(carrito);
        }
        return null;
    }

    @Transactional
    public Carrito activarCarrito(Long carritoId) {
        Carrito carrito = carritoRepository.findById(carritoId).orElse(null);
        if (carrito != null) {
            carrito.activar();
            return carritoRepository.save(carrito);
        }
        return null;
    }

    @Transactional(readOnly = true)
    public Float getMontoTotalCarrito(Long carritoId) {
        Carrito carrito = getCarritoById(carritoId);
        return carrito != null && carrito.isActivo() ? carrito.pedirMonto() : 0f;
    }
}
