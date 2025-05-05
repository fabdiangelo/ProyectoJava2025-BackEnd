package com.Tisj.services;

import com.Tisj.bussines.entities.Video;
import com.Tisj.bussines.repositories.VideoRepository;
import com.Tisj.bussines.entities.Curso;
import com.Tisj.bussines.repositories.CursoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VideoService {
    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private CursoRepository cursoRepository;

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
        // Find courses that contain the video
        List<Curso> cursos = cursoRepository.findAll().stream()
                .filter(curso -> curso.getVideos() != null && curso.getVideos().stream().anyMatch(video -> video.getId().equals(id)))
                .toList();

        // Remove the video from each course
        cursos.forEach(curso -> {
            curso.getVideos().removeIf(video -> video.getId().equals(id));
            cursoRepository.save(curso);
        });

        videoRepository.deleteById(id.intValue()); // Eliminar video por ID
    }

    public String extractVideoId(String videoLink) {
        if (videoLink == null || videoLink.trim().isEmpty()) {
            return null;
        }

        // Regular expression to extract video ID from various YouTube link formats
        String videoId = null;
        if (videoLink.contains("youtube.com/watch")) {
            // Standard YouTube link
            try {
                videoId = videoLink.split("v=")[1].split("&")[0];
            } catch (Exception e) {
                // Handle the exception if the link format is unexpected
                return null;
            }
        } else if (videoLink.contains("youtu.be")) {
            // Shortened YouTube link
            try {
                videoId = videoLink.split("youtu.be/")[1].split("?")[0];
            } catch (Exception e) {
                // Handle the exception if the link format is unexpected
                return null;
            }
        }
        // Add more conditions to handle other YouTube link formats if needed

        return videoId;
    }
}
