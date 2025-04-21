package com.Tisj.services;

import com.Tisj.bussines.entities.Video;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VideoService {

    public List<Video> getAllVideos() {
        // TODO: Implementar la lógica para obtener todos los videos
        return new ArrayList<>();
    }

    public Video getVideoById(Long id) {
        // TODO: Implementar la lógica para obtener un video por su ID
        return null;
    }

    public Video createVideo(Video video) {
        // TODO: Implementar la lógica para crear un nuevo video
        return null;
    }

    public Video updateVideo(Long id, Video video) {
        // TODO: Implementar la lógica para actualizar un video existente
        return null;
    }

    public void deleteVideo(Long id) {
        // TODO: Implementar la lógica para eliminar un video
    }
}
