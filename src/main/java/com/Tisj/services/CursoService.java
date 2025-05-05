package com.Tisj.services;

import com.Tisj.api.requests.RequestCurso;
import com.Tisj.api.requests.RequestVideo;
import com.Tisj.bussines.entities.Curso;
import com.Tisj.bussines.entities.Video;
import com.Tisj.bussines.repositories.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private VideoService videoService;

    @Autowired
    private OfertaService ofertaService;

    public List<Curso> getAllCursos() {
        return cursoRepository.findAll();
    }

    public Curso getCursoById(Long id) {
        return cursoRepository.findById(id).orElse(null);
    }

    public Curso createCurso(Curso curso) {
        return cursoRepository.save(curso);
    }

    public Curso updateCurso(Long id, Curso curso) {
        Curso modificable = getCursoById(id);
        if (modificable != null && curso != null) {
            curso.setId(id);
            curso.setPaquetes(modificable.getPaquetes());
            return cursoRepository.save(curso);
        }
        return null;
    }

    public boolean deleteCurso(Long id) {
        if (cursoRepository.existsById(id)) {
            Curso curso = getCursoById(id);
            if (curso != null) {
                // Eliminar videos que solo pertenecen a este curso
                for (Video video : curso.getVideos()) {
                    if (video.getCursos().size() == 1) { // Si solo está en este curso
                        videoService.deleteVideo(video.getId());
                    }
                }
                cursoRepository.deleteById(id);
                return true;
            }
        }
        return false;
    }

    public Curso reqToCurso(RequestCurso reqCurso) {
        List<Video> videos = new ArrayList<>();
        
        // Crear y guardar cada video
        for (RequestVideo requestVideo : reqCurso.getVideos()) {
            Video video = new Video(
                requestVideo.getNombre(),
                requestVideo.getDescripcion(),
                0f, // Duración temporalmente en 0
                requestVideo.getLink()
            );
            video = videoService.createVideo(video);
            videos.add(video);
        }

        Curso curso = new Curso(
                reqCurso.getNombre(),
                reqCurso.getDescripcion(),
                reqCurso.getPrecio(),
                reqCurso.getVideoPresentacion(),
                reqCurso.getDuracionTotal(),
                reqCurso.getEdadObj(),
                reqCurso.getGeneroObj(),
                reqCurso.getPdf(),
                videos
        );

        // Asociar oferta si se recibe el ID
        if (reqCurso.getOfertaId() != null) {
            ofertaService.getOfertaById(reqCurso.getOfertaId()).ifPresent(curso::setOferta);
        }

        return cursoRepository.save(curso);
    }

    public List<Video> getVideosCursoById(Long id) {
        Curso curso = getCursoById(id);
        if(curso == null) return null;
        return curso.getVideos();
    }

    public Curso updateVideosCurso(Long id, Long videoId) {
        Curso modificable = getCursoById(id);
        if (modificable != null) {
            if(modificable.getVideos().stream().anyMatch(v -> Objects.equals(v.getId(), videoId))){
                modificable.setVideos(modificable.getVideos().stream().filter(v -> !Objects.equals(v.getId(), videoId)).toList());
            }else {
                Video v = videoService.getVideoById(videoId);
                if(v == null){
                    return null;
                }
                modificable.getVideos().add(v);
            }
            return cursoRepository.save(modificable);
        }
        return null;
    }
}
