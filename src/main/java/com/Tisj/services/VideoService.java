package com.Tisj.services;

import com.Tisj.bussines.entities.Video;
import com.Tisj.bussines.repositories.VideoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoService {

    @Autowired
    private VideoRepository videoRepository;

    public List<Video> getAllVideos() {
        return videoRepository.findAll(); // Obtener todos los videos
    }

    public Video getVideoById(Long id) {
        return videoRepository.findById(id.intValue()).orElse(null); // Obtener video por ID
    }

    public Video createVideo(Video video) {
        return videoRepository.save(video); // Crear un nuevo video
    }

    public Video updateVideo(Long id, Video video) {
        if (videoRepository.existsById(id.intValue())) {
            video.setId(id);
            return videoRepository.save(video); // Actualizar video existente
        }
        return null;
    }

    public void deleteVideo(Long id) {
        videoRepository.deleteById(id.intValue()); // Eliminar video por ID
    }
}
