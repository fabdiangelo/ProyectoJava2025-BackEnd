package com.Tisj.services;

import com.Tisj.bussines.entities.*;
import com.Tisj.bussines.repositories.ArticuloClienteRepository;
import com.Tisj.bussines.repositories.UsuarioRepository;
import com.Tisj.bussines.repositories.ArticuloRepository;
import com.Tisj.bussines.repositories.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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

    @Autowired
    private VideoRepository videoRepository;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Tarea programada que se ejecuta diariamente a las 2:00 AM para actualizar
     * automáticamente el estado de los cursos vencidos
     */
    @Scheduled(cron = "0 0 2 * * ?") // Ejecutar diariamente a las 2:00 AM
    @Transactional
    public void actualizarCursosVencidos() {
        log.info("Iniciando actualización automática de cursos vencidos...");
        
        try {
            List<ArticuloCliente> articulosCliente = articuloClienteRepository.findAll();
            int cursosVencidos = 0;
            
            for (ArticuloCliente ac : articulosCliente) {
                if (ac.getEstado() != ArticuloCliente.Estado.CADUCADO && 
                    LocalDate.now().isAfter(ac.getCaducidad())) {
                    ac.caducar();
                    articuloClienteRepository.save(ac);
                    cursosVencidos++;
                    log.debug("Curso vencido actualizado: ID={}, Usuario={}, Curso={}", 
                        ac.getId(), ac.getUsuario().getEmail(), ac.getArticulo().getNombre());
                }
            }
            
            log.info("Actualización completada. {} cursos marcados como vencidos.", cursosVencidos);
        } catch (Exception e) {
            log.error("Error durante la actualización automática de cursos vencidos: {}", e.getMessage(), e);
        }
    }

    /**
     * Método para obtener cursos próximos a vencer (dentro de los próximos 7 días)
     */
    @Transactional(readOnly = true)
    public List<ArticuloCliente> getCursosProximosAVencer(String email) {
        LocalDate fechaLimite = LocalDate.now().plusDays(7);
        return articuloClienteRepository.findByUsuarioEmailAndEstadoAndCaducidadBefore(email, 
            ArticuloCliente.Estado.ACTIVO, fechaLimite);
    }

    /**
     * Método para obtener cursos vencidos de un usuario
     */
    @Transactional(readOnly = true)
    public List<ArticuloCliente> getCursosVencidos(String email) {
        return articuloClienteRepository.findByUsuarioEmailAndEstado(email, ArticuloCliente.Estado.CADUCADO);
    }

    /**
     * Método para renovar la caducidad de un curso (extender por 3 meses más)
     */
    @Transactional
    public ArticuloCliente renovarCaducidad(Long articuloClienteId) {
        ArticuloCliente articuloCliente = articuloClienteRepository.findById(articuloClienteId).orElse(null);
        if (articuloCliente != null) {
            articuloCliente.reiniciar(); // Esto establece la nueva caducidad a 3 meses desde hoy
            articuloCliente = articuloClienteRepository.save(articuloCliente);
            log.info("Caducidad renovada para curso: ID={}, Usuario={}, Nueva caducidad={}", 
                articuloClienteId, articuloCliente.getUsuario().getEmail(), articuloCliente.getCaducidad());
            return articuloCliente;
        }
        return null;
    }

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

    @Transactional
    public ArticuloCliente marcarVideoComoVisto(Long articuloClienteId, Long videoId) {
        ArticuloCliente ac = articuloClienteRepository.findById(articuloClienteId).orElse(null);
        if (ac == null) {
            throw new IllegalArgumentException("Inscripción no encontrada.");
        }

        // Optional: Validate that the video belongs to the course associated with this ArticuloCliente
        boolean videoExists = videoRepository.existsById(videoId);
        if (!videoExists) {
            throw new IllegalArgumentException("Video no encontrado.");
        }

        ac.getVideosVistos().add(videoId);
        return articuloClienteRepository.save(ac);
    }

    public DTArticuloCliente toDTO(ArticuloCliente ac) {
        double progreso = 0.0;
        if (ac.getArticulo() instanceof com.Tisj.bussines.entities.Curso) {
            com.Tisj.bussines.entities.Curso curso = (com.Tisj.bussines.entities.Curso) ac.getArticulo();
            int totalVideos = curso.getVideos().size();
            if (totalVideos > 0) {
                progreso = ((double) ac.getVideosVistos().size() / totalVideos) * 100;
            }
        }

        return new DTArticuloCliente(
            ac.getId(),
            ac.getCaducidad(),
            ac.getEstado().name(),
            ac.getActivo(),
            ac.getArticulo().getId(),
            ac.getUsuario().getEmail(),
            progreso,
            ac.getVideosVistos()
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

        ArticuloCliente ac = new ArticuloCliente(articulo, usuario);

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

    @Transactional
    public void procesarCompra(String email, Long articuloId) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Articulo articulo = articuloRepository.findById(articuloId)
                .orElseThrow(() -> new IllegalArgumentException("Artículo no encontrado"));

        if (articulo instanceof Paquete paquete) {
            // Si es un paquete, procesá cada curso dentro del paquete
            for (Curso curso : paquete.getCursos()) {
                asignarOExtender(usuario, curso);
            }
        } else if (articulo instanceof Curso curso) {
            // Si es un curso, procesalo directamente
            asignarOExtender(usuario, curso);
        } else {
            throw new IllegalStateException("Tipo de artículo desconocido: " + articulo.getClass());
        }
    }

    private void asignarOExtender(Usuario usuario, Articulo articulo) {
        Optional<ArticuloCliente> existenteOpt =
                articuloClienteRepository.findByUsuarioAndArticulo(usuario, articulo);

        if (existenteOpt.isPresent()) {
            ArticuloCliente existente = existenteOpt.get();
            // extendemos 3 meses más
            existente.setCaducidad(existente.getCaducidad().plusMonths(3));
            articuloClienteRepository.save(existente);
        } else {
            ArticuloCliente nuevo = new ArticuloCliente();
            nuevo.setUsuario(usuario);
            nuevo.setArticulo(articulo);
            nuevo.setEstado(ArticuloCliente.Estado.ACTIVO);
            nuevo.setCaducidad(LocalDate.now().plusMonths(3));
            articuloClienteRepository.save(nuevo);
        }
    }
}
