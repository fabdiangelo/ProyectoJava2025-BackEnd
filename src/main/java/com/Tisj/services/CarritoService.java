package com.Tisj.services;

import com.Tisj.bussines.entities.Carrito;
import com.Tisj.bussines.repositories.CarritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    public Carrito createCarrito(Carrito carrito) {
        return carritoRepository.save(carrito); // Crear un nuevo carrito
    }

    public Carrito getCarritoById(Long id) {
        return carritoRepository.findById(id).orElse(null); // Obtener un carrito por su ID
    }

    public List<Carrito> getAllCarritos() {
        return carritoRepository.findAll(); // Obtener todos los carritos
    }

    // public Carrito updateCarrito(Long id, Carrito carrito) {
    //     if (carritoRepository.existsById(id)) {
    //         carrito.setId(id);
    //         return carritoRepository.save(carrito); // Actualizar un carrito existente
    //     }
    //     return null;
    // }

    public void deleteCarrito(Long id) {
        if (carritoRepository.existsById(id)) {
            carritoRepository.deleteById(id); // Eliminar un carrito por su ID
        }
    }

    public Carrito getCarritoByUsuarioEmail(String email) {
        return carritoRepository.findByUsuarioEmail(email).orElse(null); // Obtener un carrito por el email del usuario
    }

    public Object updateCarrito(Long id, Carrito carrito) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateCarrito'");
    }
}
