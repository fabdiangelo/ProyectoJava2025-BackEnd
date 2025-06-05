package com.Tisj.services;

import com.Tisj.bussines.entities.ArticuloCliente;
import com.Tisj.bussines.entities.Usuario;
import com.Tisj.bussines.entities.Articulo;
import com.Tisj.bussines.repositories.ArticuloClienteRepository;
import com.Tisj.bussines.repositories.UsuarioRepository;
import com.Tisj.bussines.repositories.ArticuloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;
import com.Tisj.bussines.entities.DT.DTArticuloCliente;

@Service
@Transactional
public class ArticuloClienteService {

    private static final Logger log = LoggerFactory.getLogger(ArticuloClienteService.class);

    @Autowired
    private ArticuloClienteRepository articuloClienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ArticuloRepository articuloRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<ArticuloCliente> getAllArticulosCliente() {
        List<ArticuloCliente> articulosCliente = articuloClienteRepository.findAll();
        articulosCliente.forEach(ac -> {
            ac.actualizarEstadoPorFecha();
            articuloClienteRepository.save(ac);
            if (ac.getArticulo() != null && ac.getArticulo().getOferta() != null) {
                ac.getArticulo().getOferta().getArticulos().size(); // Forzar la inicialización
            }
        });
        return articulosCliente;
    }

    @Transactional(readOnly = true)
    public ArticuloCliente getArticuloClienteById(Long id) {
        ArticuloCliente articuloCliente = articuloClienteRepository.findById(id).orElse(null);
        if (articuloCliente != null) {
            articuloCliente.actualizarEstadoPorFecha();
            articuloClienteRepository.save(articuloCliente);
            if (articuloCliente.getArticulo() != null && articuloCliente.getArticulo().getOferta() != null) {
                articuloCliente.getArticulo().getOferta().getArticulos().size(); // Forzar la inicialización
            }
        }
        return articuloCliente;
    }

    public ArticuloCliente createArticuloCliente(ArticuloCliente articuloCliente) {
        articuloCliente.setEstado(ArticuloCliente.Estado.ACTIVO);
        articuloCliente.setCaducidad(java.time.LocalDate.now().plusMonths(3));
        return articuloClienteRepository.save(articuloCliente);
    }

    public ArticuloCliente updateArticuloCliente(Long id, ArticuloCliente articuloCliente) {
        ArticuloCliente articuloClienteExistente = articuloClienteRepository.findById(id).orElse(null);
        if (articuloClienteExistente != null) {
            articuloCliente.setId(articuloClienteExistente.getId());
            return articuloClienteRepository.save(articuloCliente);
        }
        return null;
    }

    public void deleteArticuloCliente(Long id) {
        articuloClienteRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ArticuloCliente> getArticulosClienteByUsuarioEmail(String email) {
        List<ArticuloCliente> articulosCliente = articuloClienteRepository.findByUsuarioEmail(email);
        articulosCliente.forEach(ac -> {
            ac.actualizarEstadoPorFecha();
            articuloClienteRepository.save(ac);
            if (ac.getArticulo() != null && ac.getArticulo().getOferta() != null) {
                ac.getArticulo().getOferta().getArticulos().size(); // Forzar la inicialización
            }
        });
        return articulosCliente;
    }

    public ArticuloCliente comprarArticulo(String email, Long articuloId) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
        Articulo articulo = articuloRepository.findById(articuloId).orElse(null);

        if (usuario == null || articulo == null) {
            return null;
        }

        // Verificar si ya tiene el artículo
        if (articuloClienteRepository.existsByUsuarioAndArticulo(usuario, articulo)) {
            return null;
        }

        ArticuloCliente articuloCliente = new ArticuloCliente(articulo, usuario);
        articuloCliente.setEstado(ArticuloCliente.Estado.ACTIVO);
        articuloCliente.setCaducidad(java.time.LocalDate.now().plusMonths(3));
        return articuloClienteRepository.save(articuloCliente);
    }

    @Transactional(readOnly = true)
    public boolean verificarAcceso(String email, Long articuloId) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
        Articulo articulo = articuloRepository.findById(articuloId).orElse(null);

        if (usuario == null || articulo == null) {
            return false;
        }

        return articuloClienteRepository.existsByUsuarioAndArticuloAndActivoTrue(usuario, articulo);
    }

    public ArticuloCliente completarArticuloCliente(Long id) {
        ArticuloCliente articuloCliente = articuloClienteRepository.findById(id).orElse(null);
        if (articuloCliente != null && articuloCliente.getEstado() != ArticuloCliente.Estado.CADUCADO) {
            articuloCliente.setEstado(ArticuloCliente.Estado.COMPLETO);
            return articuloClienteRepository.save(articuloCliente);
        }
        return null;
    }

    public ArticuloCliente reiniciarArticuloCliente(Long id) {
        ArticuloCliente articuloCliente = articuloClienteRepository.findById(id).orElse(null);
        if (articuloCliente != null) {
            articuloCliente.reiniciar();
            return articuloClienteRepository.save(articuloCliente);
        }
        return null;
    }

    public DTArticuloCliente toDTO(ArticuloCliente ac) {
        return new DTArticuloCliente(
            ac.getId(),
            ac.getCaducidad(),
            ac.getEstado().name(),
            ac.getActivo(),
            ac.getArticulo().getId(),
            ac.getUsuario().getEmail()
        );
    }

    public List<DTArticuloCliente> getArticulosClienteByUsuarioEmailDTO(String email) {
        return getArticulosClienteByUsuarioEmail(email)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public DTArticuloCliente getArticuloClienteByIdDTO(Long id) {
        ArticuloCliente ac = getArticuloClienteById(id);
        return ac != null ? toDTO(ac) : null;
    }

    public DTArticuloCliente createArticuloClienteForUser(String userEmail, Long articuloId) {
        Usuario usuario = usuarioRepository.findByEmail(userEmail).orElse(null);
        if (usuario == null) {
            log.warn("Usuario no encontrado con email: {}", userEmail);
            throw new IllegalArgumentException("Usuario no encontrado con email: " + userEmail);
        }
        Articulo articulo = articuloRepository.findById(articuloId).orElse(null);
        if (articulo == null) {
            log.warn("Artículo no encontrado con id: {}", articuloId);
            throw new IllegalArgumentException("Artículo no encontrado con id: " + articuloId);
        }

        // Verificar si ya tiene el artículo
        if (articuloClienteRepository.existsByUsuarioAndArticulo(usuario, articulo)) {
            log.warn("El usuario {} ya tiene el artículo {}", userEmail, articuloId);
            // Retornar null para indicar que el artículo ya existe para este usuario
            return null;
        }

        ArticuloCliente ac = new ArticuloCliente();
        ac.setArticulo(articulo);
        ac.setUsuario(usuario);
        ac.setActivo(true); // Set activo to true by default
        ac.setCaducidad(java.time.LocalDate.now().plusMonths(3)); // Set caducidad to 3 months from now
        ac.setEstado(ArticuloCliente.Estado.ACTIVO); // Set estado to INCOMPLETO

        ArticuloCliente guardado = articuloClienteRepository.save(ac);
        return toDTO(guardado);
    }

    public List<DTArticuloCliente> getArticulosClienteByUsuarioEmailDT(String email) {
        List<ArticuloCliente> articulosCliente = articuloClienteRepository.findByUsuarioEmail(email);
        return articulosCliente.stream().map(this::toDTO).collect(java.util.stream.Collectors.toList());
    }

    public DTArticuloCliente getArticuloClienteByIdDT(Long id) {
        ArticuloCliente ac = articuloClienteRepository.findById(id).orElse(null);
        return ac != null ? toDTO(ac) : null;
    }
}
