package com.Tisj.services;

import com.Tisj.bussines.entities.Carrito;
import com.Tisj.bussines.entities.Articulo;
import com.Tisj.bussines.entities.ArticuloCliente;
import com.Tisj.bussines.entities.DT.DTCarrito;
import com.Tisj.bussines.repositories.CarritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ArticuloService articuloService;

    @Autowired
    private ArticuloClienteService articuloClienteService;

    private DTCarrito convertirADTO(Carrito carrito) {
        if (carrito == null) return null;
        return new DTCarrito(
            carrito.getId(),
            carrito.getVencimiento(),
            carrito.isActivo(),
            carrito.getItems().stream().map(Articulo::getId).collect(Collectors.toList()),
            carrito.getPago() != null ? carrito.getPago().getId() : null,
            carrito.getUsuarioId(),
            carrito.getMontoTotal()
        );
    }

    public DTCarrito createCarrito(Carrito carrito) {
        try {
            if (carrito == null) {
                throw new IllegalArgumentException("El carrito no puede ser nulo");
            }
            carrito.setActivo(true);
            carrito.renovarVencimiento();
            return convertirADTO(carritoRepository.save(carrito));
        } catch (Exception e) {
            log.error("Error al crear carrito: {}", e.getMessage());
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public DTCarrito getCarritoById(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("El ID del carrito no puede ser nulo");
            }
            Carrito carrito = carritoRepository.findById(id)
                    .filter(Carrito::isActivo)
                    .orElse(null);
            if (carrito != null) {
                carrito.getItems().size(); // Forzar carga de items
            }
            return convertirADTO(carrito);
        } catch (Exception e) {
            log.error("Error al obtener carrito con ID {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public List<DTCarrito> getAllCarritos() {
        try {
            List<Carrito> carritos = carritoRepository.findAll().stream()
                    .filter(Carrito::isActivo)
                    .toList();
            carritos.forEach(carrito -> carrito.getItems().size());
            return carritos.stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al obtener todos los carritos: {}", e.getMessage());
            throw e;
        }
    }

    public void deleteCarrito(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("El ID del carrito no puede ser nulo");
            }
            Carrito carrito = carritoRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Carrito no encontrado"));
            carrito.setActivo(false);
            carritoRepository.save(carrito);
            log.info("Carrito {} eliminado exitosamente", id);
        } catch (Exception e) {
            log.error("Error al eliminar carrito {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public DTCarrito getCarritoByUsuarioEmail(String email) {
        try {
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("El email del usuario no puede ser nulo o vacío");
            }
            Carrito carrito = carritoRepository.findByUsuarioEmailAndActivoTrue(email).orElse(null);
            if (carrito != null) {
                carrito.getItems().size();
            }
            return convertirADTO(carrito);
        } catch (Exception e) {
            log.error("Error al obtener carrito para usuario {}: {}", email, e.getMessage());
            throw e;
        }
    }

    @Transactional
    public DTCarrito agregarItemAlCarrito(Long carritoId, Long articuloId) {
        try {
            if (carritoId == null) {
                throw new IllegalArgumentException("El ID del carrito no puede ser nulo");
            }
            if (articuloId == null) {
                throw new IllegalArgumentException("El ID del artículo no puede ser nulo");
            }

            Carrito carrito = carritoRepository.findById(carritoId)
                    .orElseThrow(() -> new IllegalArgumentException("Carrito no encontrado"));
            
            Articulo articulo = articuloService.getArticuloById(articuloId);
            if (articulo == null) {
                throw new IllegalArgumentException("Artículo no encontrado");
            }

            if (carrito.isVencido()) {
                carrito.renovarVencimiento();
            }

            Articulo agregado = carrito.agregarAlCarrito(articulo);
            if (agregado != null) {
                return convertirADTO(carritoRepository.save(carrito));
            }
            return null;
        } catch (Exception e) {
            log.error("Error al agregar artículo {} al carrito {}: {}", articuloId, carritoId, e.getMessage());
            throw e;
        }
    }

    @Transactional
    public DTCarrito quitarItemDelCarrito(Long carritoId, Long articuloId) {
        try {
            if (carritoId == null) {
                throw new IllegalArgumentException("El ID del carrito no puede ser nulo");
            }
            if (articuloId == null) {
                throw new IllegalArgumentException("El ID del artículo no puede ser nulo");
            }

            Carrito carrito = carritoRepository.findById(carritoId)
                    .orElseThrow(() -> new IllegalArgumentException("Carrito no encontrado"));

            if (carrito.quitarElementoDelCarrito(articuloId)) {
                return convertirADTO(carritoRepository.save(carrito));
            }
            return null;
        } catch (Exception e) {
            log.error("Error al quitar artículo {} del carrito {}: {}", articuloId, carritoId, e.getMessage());
            throw e;
        }
    }

    @Transactional
    public DTCarrito desactivarCarrito(Long carritoId) {
        try {
            Carrito carrito = carritoRepository.findById(carritoId)
                    .orElseThrow(() -> new IllegalArgumentException("Carrito no encontrado"));
            
            // TODO: Eliminar esta línea - Verificación de pago eliminada temporalmente para pruebas.
            // if (carrito.getPago() == null) {
            //     throw new IllegalStateException("No se puede cerrar un carrito sin pago asociado");
            // }

            // Procesar la compra antes de desactivar el carrito
            procesarCompraCarrito(carrito);
            
            carrito.desactivar();
            return convertirADTO(carritoRepository.save(carrito));
        } catch (Exception e) {
            log.error("Error al desactivar carrito {}: {}", carritoId, e.getMessage());
            throw e;
        }
    }

    @Transactional
    private void procesarCompraCarrito(Carrito carrito) {
        if (carrito == null || carrito.getItems() == null || carrito.getItems().isEmpty()) {
            throw new IllegalArgumentException("El carrito está vacío o es inválido");
        }

        String emailUsuario = carrito.getUsuarioId();
        if (emailUsuario == null) {
            throw new IllegalStateException("El carrito no tiene un usuario asociado");
        }

        // Por cada artículo en el carrito, crear un ArticuloCliente
        for (Articulo articulo : carrito.getItems()) {
            try {
                // Intentar comprar el artículo
                ArticuloCliente resultado = articuloClienteService.comprarArticulo(emailUsuario, articulo.getId());
                
                if (resultado != null) {
                    log.info("Artículo {} agregado al usuario {}", articulo.getId(), emailUsuario);
                } else {
                    // Si el artículo ya existe para el usuario, verificamos si está activo
                    // y si no lo está, lo reactivamos
                    log.info("El artículo {} ya existe para el usuario {}. Verificando estado...", 
                        articulo.getId(), emailUsuario);
                    
                    // Usar createArticuloClienteForUser que maneja mejor este caso
                    articuloClienteService.createArticuloClienteForUser(emailUsuario, articulo.getId());
                    log.info("Artículo {} procesado para el usuario {}", articulo.getId(), emailUsuario);
                }
            } catch (Exception e) {
                log.error("Error al procesar artículo {} para usuario {}: {}", 
                    articulo.getId(), emailUsuario, e.getMessage());
                throw new RuntimeException("Error al procesar la compra: " + e.getMessage());
            }
        }
    }

    @Transactional
    public DTCarrito activarCarrito(Long carritoId) {
        Carrito carrito = carritoRepository.findById(carritoId).orElse(null);
        if (carrito != null) {
            carrito.activar();
            return convertirADTO(carritoRepository.save(carrito));
        }
        return null;
    }

    public Float getMontoTotalCarrito(Long carritoId) {
        try {
            if (carritoId == null) {
                throw new IllegalArgumentException("El ID del carrito no puede ser nulo");
            }
            Carrito carrito = carritoRepository.findById(carritoId)
                    .orElseThrow(() -> new IllegalArgumentException("Carrito no encontrado"));
            return carrito.getMontoTotal();
        } catch (Exception e) {
            log.error("Error al obtener monto total del carrito {}: {}", carritoId, e.getMessage());
            throw e;
        }
    }
}
