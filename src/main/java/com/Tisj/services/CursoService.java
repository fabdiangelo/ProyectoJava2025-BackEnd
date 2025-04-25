package com.Tisj.services;

import com.Tisj.api.requests.RequestCurso;
import com.Tisj.bussines.entities.Curso;
import com.Tisj.bussines.entities.Video;
import com.Tisj.bussines.repositories.CursoRepository;
import com.Tisj.bussines.repositories.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private VideoService videoService;

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
            cursoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Curso reqToCurso(RequestCurso reqCurso) {
        List<Video> videos = reqCurso.getVideoIds()
                .stream()
                .map(id ->
                        videoService.getVideoById(id)
                )
                .toList();

        if(videos.stream().anyMatch(Objects::isNull)){
            return null;
        }

        return new Curso(
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
