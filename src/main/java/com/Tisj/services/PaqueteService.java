package com.Tisj.services;

import com.Tisj.bussines.entities.Paquete;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaqueteService {

    public List<Paquete> getAllPaquetes() {
        // TODO: Implementar la lógica para obtener todos los paquetes
        return new ArrayList<>();
    }

    public Paquete getPaqueteById(Long id) {
        // TODO: Implementar la lógica para obtener un paquete por su ID
        return null;
    }

    public Paquete createPaquete(Paquete paquete) {
        // TODO: Implementar la lógica para crear un nuevo paquete
        return null;
    }

    public Paquete updatePaquete(Long id, Paquete paquete) {
        // TODO: Implementar la lógica para actualizar un paquete existente
        return null;
    }

    public void deletePaquete(Long id) {
        // TODO: Implementar la lógica para eliminar un paquete
    }
}
