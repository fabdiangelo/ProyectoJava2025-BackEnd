package com.Tisj.services;

import com.Tisj.bussines.entities.Pago;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PagoService {

    public List<Pago> getAllPagos() {
        // TODO: Implementar la lógica para obtener todos los pagos
        return new ArrayList<>();
    }

    public Pago getPagoById(Long id) {
        // TODO: Implementar la lógica para obtener un pago por su ID
        return null;
    }

    
    public Pago createPago(Pago pago) {
        // TODO: Implementar la lógica para crear un nuevo pago
        return null;
    }

    public Pago updatePago(Long id, Pago pago) {
        // TODO: Implementar la lógica para actualizar un pago existente
        return null;
    }

    public void deletePago(Long id) {
        // TODO: Implementar la lógica para eliminar un pago
    }
}