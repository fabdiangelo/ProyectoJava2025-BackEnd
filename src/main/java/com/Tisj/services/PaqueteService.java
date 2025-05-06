package com.Tisj.services;

import com.Tisj.api.requests.RequestPaquete;
import com.Tisj.bussines.entities.Curso;
import com.Tisj.bussines.entities.Paquete;
import com.Tisj.bussines.repositories.PaqueteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class PaqueteService {

    @Autowired
    private CursoService cursoService;

    @Autowired
    private PaqueteRepository paqueteRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<Paquete> getAllPaquetes() {
        List<Paquete> paquetes = paqueteRepository.findByActivoTrue();
        paquetes.forEach(p -> p.getCursos().size()); // Forzar la inicialización
        return paquetes;
    }

    @Transactional(readOnly = true)
    public Paquete getPaqueteById(Long id) {
        Paquete paquete = paqueteRepository.findByIdAndActivoTrue(id);
        if (paquete != null) {
            paquete.getCursos().size(); // Forzar la inicialización
        }
        return paquete;
    }

    public Paquete reqToPaquete(RequestPaquete reqPaquete) {
        List<Curso> cursos = reqPaquete.getCursoIds()
                .stream()
                .map(id -> cursoService.getCursoById(id))
                .toList();

        if(cursos.stream().anyMatch(Objects::isNull)){
            return null;
        }

        Paquete paquete = new Paquete(
                reqPaquete.getNombre(),
                reqPaquete.getDescripcion(),
                reqPaquete.getPrecio(),
                reqPaquete.getVideoPresentacion(),
                cursos
        );
        paquete.setActivo(true);
        return paquete;
    }

    public Paquete createPaquete(Paquete paquete) {
        paquete.setActivo(true);
        return paqueteRepository.save(paquete);
    }

    public Paquete updatePaquete(Long id, Paquete paquete) {
        Paquete modificable = getPaqueteById(id);
        if (modificable != null && paquete != null) {
            paquete.setId(id);
            paquete.setCursos(modificable.getCursos());
            paquete.setActivo(true);
            return paqueteRepository.save(paquete);
        }
        return null;
    }

    public boolean deletePaquete(Long id) {
        Paquete paquete = getPaqueteById(id);
        if (paquete != null) {
            paquete.setActivo(false);
            paqueteRepository.save(paquete);
            return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public List<Curso> getCursosDelPaquete(Long id) {
        Paquete paquete = getPaqueteById(id);
        if(paquete == null) return null;
        return paquete.getCursos();
    }

    public Paquete updateCursosPaquete(Long id, Long cursoId) {
        Paquete modificable = getPaqueteById(id);
        if (modificable != null) {
            if(modificable.getCursos().stream().anyMatch(c -> Objects.equals(c.getId(), cursoId))){
                modificable.setCursos(modificable.getCursos().stream().filter(c -> !Objects.equals(c.getId(), cursoId)).toList());
            }else {
                Curso c = cursoService.getCursoById(cursoId);
                if(c == null){
                    return null;
                }
                modificable.getCursos().add(c);
            }
            return paqueteRepository.save(modificable);
        }
        return null;
    }

    // public void associateCursoToPaquete(Long paqueteId, Long cursoId) {
    //     Paquete paquete = ((Object) paqueteRepository.findById(paqueteId))
    //             .orElseThrow(() -> new IllegalArgumentException("Paquete no encontrado con ID: " + paqueteId));
    //     Curso curso = cursoRepository.findById(cursoId)
    //             .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado con ID: " + cursoId));

    //     paquete.getCursos().add(curso); // Asociar el curso al paquete
    //     paqueteRepository.save(paquete); // Guardar los cambios
    // }

    // public void disassociateCursoFromPaquete(Long paqueteId, Long cursoId) {
    //     String response = null;
    //     if(paqueteId != null){
    //         Optional<Paquete> user = Pauq.findById(paqueteId);
    //         if(user.isPresent() && user.get().getRoles().stream().noneMatch(rolUsuario -> rolUsuario.getNombre().equals("ADMIN"))){
    //             usuarioRepository.deleteByEmail(email);
    //             response = "no existe el paquete con el id " + paqueteId;
    //         }
    //         if(cursoId != null){
    //             Optional<Usuario> user = usuarioRepository.findById(email);
    //             if(user.isPresent() && user.get().getRoles().stream().noneMatch(rolUsuario -> rolUsuario.getNombre().equals("ADMIN"))){
    //                 usuarioRepository.deleteByEmail(email);
    //                 response = "Cliente eliminado: " + email;
    //             }
    //         }
    //     }
    //     }
        
      

    
}
