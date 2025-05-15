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
        List<Video> videos = videoRepository.findByActivoTrue();
        for (Video video : videos) {
            String videoId = extractVideoId(video.getLink());
            if (videoId != null) {
                video.setLink(buildYoutubeUrl(videoId));
            }
        }
        return videos;
    }

    public Video getVideoById(Long id) {
        Video video = videoRepository.findByIdAndActivoTrue(id);
        if (video != null) {
            String videoId = extractVideoId(video.getLink());
            if (videoId != null) {
                video.setLink(buildYoutubeUrl(videoId));
            }
        }
        return video;
    }

    public Video createVideo(Video video) {
        if (video.getCurso() == null || video.getCurso().getId() == null) {
            throw new IllegalArgumentException("El video debe estar asociado a un curso válido");
        }
        
        Curso curso = cursoRepository.findById(video.getCurso().getId())
            .orElseThrow(() -> new IllegalArgumentException("El curso especificado no existe"));
            
        video.setCurso(curso);
        String videoId = extractVideoId(video.getLink());
        if (videoId != null) {
            video.setLink(buildYoutubeUrl(videoId));
        }
        video.setActivo(true);
        return videoRepository.save(video);
    }

    public boolean deleteVideo(Long id) {
        Video video = getVideoById(id);
        if (video != null) {
            video.setActivo(false);
            videoRepository.save(video);
            return true;
        }
        return false;
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
                return null;
            }
        } else if (videoLink.contains("youtu.be")) {
            // Shortened YouTube link
            try {
                videoId = videoLink.split("youtu.be/")[1].split("?")[0];
            } catch (Exception e) {
                return null;
            }
        } else if (videoLink.matches("^[a-zA-Z0-9_-]{11}$")) {
            // Direct video ID
            videoId = videoLink;
        }

        return videoId;
    }

    public String buildYoutubeUrl(String videoId) {
        if (videoId == null || videoId.trim().isEmpty()) {
            return null;
        }
        return String.format("https://www.youtube.com/watch?v=%s", videoId);
    }
}
