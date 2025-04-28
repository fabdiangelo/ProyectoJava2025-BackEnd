package com.Tisj.services;


import com.Tisj.bussines.entities.Paquete;
import com.Tisj.bussines.repositories.CursoRepository;
import com.Tisj.bussines.repositories.PaqueteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaqueteService {

    @Autowired
    private ArticuloService articuloService;

    @Autowired
    private PaqueteRepository paqueteRepository;

    @Autowired
    private CursoRepository cursoRepository;

    public List<Paquete> getAllPaquetes() {
        return (List<Paquete>) (List<?>) articuloService.getAllArticulos();
    }

    public Paquete getPaqueteById(Long id) {
        return (Paquete) articuloService.getArticuloById(id);
    }

    public Paquete createPaquete(Paquete paquete) {
        return (Paquete) articuloService.createArticulo(paquete);
    }

    public Paquete updatePaquete(Long id, Paquete paquete) {
        return (Paquete) articuloService.updateArticulo(id, paquete);
    }

    public void deletePaquete(Long id) {
        articuloService.deleteArticulo(id);
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
