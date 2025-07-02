package com.Tisj.services;

import com.Tisj.api.requests.RequestCurso;
import com.Tisj.api.requests.RequestVideo;
import com.Tisj.bussines.entities.Curso;
import com.Tisj.bussines.entities.Video;
import com.Tisj.bussines.repositories.CursoRepository;
import com.Tisj.bussines.repositories.VideoRepository;
import com.Tisj.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private VideoService videoService;

    @Autowired
    private OfertaService ofertaService;
    @Autowired
    private VideoRepository videoRepository;

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

    @Transactional
    public Curso updateCurso(Long id, RequestCurso reqCurso) {
        Optional<Curso> optCurso = cursoRepository.findById(id);
        if (optCurso.isEmpty()) {
            return null;
        }
        Curso cursoExistente = optCurso.get();

        // 1. Actualizar campos simples
        if (reqCurso.getNombre() != null)
            cursoExistente.setNombre(reqCurso.getNombre());
        if (reqCurso.getDescripcion() != null)
            cursoExistente.setDescripcion(reqCurso.getDescripcion());
        if (reqCurso.getPrecio() != null)
            cursoExistente.setPrecio(reqCurso.getPrecio());
        if (reqCurso.getVideoPresentacion() != null)
            cursoExistente.setVideoPresentacion(reqCurso.getVideoPresentacion());
        if (reqCurso.getActivo() != null)
            cursoExistente.setActivo(reqCurso.getActivo());

        // 2. Vaciar la colección gestionada (mismos objetos List)
        List<Video> videosGestión = cursoExistente.getVideos();
        videosGestión.clear();

        // 3. Añadir cada nuevo Video a la misma lista
        for (RequestVideo rv : reqCurso.getVideos()) {
            Video video = new Video(
                    rv.getNombre(),
                    rv.getDescripcion(),
                    0f,
                    rv.getLink()
            );
            video.setCurso(cursoExistente);
            videosGestión.add(video);
        }

        // 4. Guardar: por el cascade, los videos se insertan y los viejos se eliminan
        return cursoRepository.save(cursoExistente);
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
        //cursoRepository.save(curso);

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
            //videoService.save(video);
            video = videoService.createVideo(video);
            videos.add(video);
        }

        // 3. Asignar los videos al curso y guardar el curso actualizado
        curso.setVideos(videos);

        if(reqCurso.getActivo() != null) {
            reqCurso.setActivo(reqCurso.getActivo());
        }else{
            reqCurso.setActivo(true);
        }
        return curso;
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

    @Transactional
    public Curso nuevoCurso(RequestCurso reqCurso) {
        // 1. Crear la entidad Curso y setear campos básicos
        Curso curso = new Curso();
        curso.setNombre(reqCurso.getNombre());
        curso.setDescripcion(reqCurso.getDescripcion());
        curso.setPrecio(reqCurso.getPrecio());
        curso.setVideoPresentacion(reqCurso.getVideoPresentacion());
        curso.setActivo(reqCurso.getActivo() != null ? reqCurso.getActivo() : true);

        // 2. Construir la lista gestionada de videos (misma instancia de List)
        List<Video> listaVideos = curso.getVideos();
        for (RequestVideo rv : reqCurso.getVideos()) {
            Video video = new Video(
                    rv.getNombre(),
                    rv.getDescripcion(),
                    0f,
                    rv.getLink()
            );
            video.setCurso(curso);
            listaVideos.add(video);
        }

        // 3. Guardar el curso (por cascade = ALL se persisten también los videos)
        return cursoRepository.save(curso);
    }
}
