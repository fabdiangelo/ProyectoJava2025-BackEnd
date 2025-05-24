package com.Tisj.services;

import com.Tisj.api.requests.RequestCurso;
import com.Tisj.api.requests.RequestVideo;
import com.Tisj.bussines.entities.Curso;
import com.Tisj.bussines.entities.Video;
import com.Tisj.bussines.repositories.CursoRepository;
import com.Tisj.services.VideoService;
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
        return cursoRepository.findByActivoTrue();
    }

    public Curso getCursoById(Long id) {
        return cursoRepository.findByIdAndActivoTrue(id);
    }

    public Curso createCurso(Curso curso) {
        curso.setActivo(true);
        return cursoRepository.save(curso);
    }

    public Curso updateCurso(Long id, Curso curso) {
        Curso modificable = getCursoById(id);
        if (modificable != null && curso != null) {
            curso.setId(id);
            curso.setPaquetes(modificable.getPaquetes());
            curso.setActivo(true);
            return cursoRepository.save(curso);
        }
        return null;
    }

    public boolean deleteCurso(Long id) {
        Curso curso = getCursoById(id);
        if (curso != null) {
            curso.setActivo(false);
            cursoRepository.save(curso);
            return true;
        }
        return false;
    }

    public Curso reqToCurso(RequestCurso reqCurso) {
        // 1. Crear el curso sin videos
        Curso curso = new Curso(
            reqCurso.getNombre(),
            reqCurso.getDescripcion(),
            reqCurso.getPrecio(),
            reqCurso.getVideoPresentacion()
        );

        // Guardar el curso para obtener el ID
        curso = cursoRepository.save(curso);

        // 2. Crear y asociar videos
        List<Video> videos = new ArrayList<>();
        for (RequestVideo requestVideo : reqCurso.getVideos()) {
            Video video = new Video(
                requestVideo.getNombre(),
                requestVideo.getDescripcion(),
                0f,
                requestVideo.getLink()
            );
            video.setCurso(curso); // Asignar el curso al video
            videoService.save(video);
            video = videoService.createVideo(video);
            videos.add(video);
        }

        // 3. Asignar los videos al curso y guardar el curso actualizado
        curso.setVideos(videos);
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

    public Curso getCursoByNombre(String nombre) {
        return cursoRepository.findByNombre(nombre);
    }
}
