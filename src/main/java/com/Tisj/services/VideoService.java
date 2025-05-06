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
        return videoRepository.findByActivoTrue();
    }

    public Video getVideoById(Long id) {
        return videoRepository.findByIdAndActivoTrue(id);
    }

    public Video createVideo(Video video) {
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
