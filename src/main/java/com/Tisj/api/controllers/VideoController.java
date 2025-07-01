package com.Tisj.api.controllers;

import com.Tisj.api.response.YoutubeVideoDetails;
import com.Tisj.bussines.entities.Video;
import com.Tisj.exceptions.YoutubeApiException;
import com.Tisj.services.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import com.Tisj.services.YoutubeService;
import org.springframework.beans.factory.annotation.Autowired;
@RestController
@RequestMapping("/api/videos")
public class VideoController {
    
    @Autowired
    private VideoService videoService;

   
    @Autowired
    private YoutubeService youtubeService;

    @GetMapping
    public ResponseEntity<List<Video>> getVideos() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN") || p.getAuthority().equals("USER"))) {
            List<Video> videos = videoService.getAllVideos();
            return new ResponseEntity<>(videos, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Video> getVideo(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN") || p.getAuthority().equals("USER"))) {
            Video video = videoService.getVideoById(id);
            if (video != null) {
                return new ResponseEntity<>(video, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/{id}/youtube")
    public ResponseEntity<?> getYoutubeVideo(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN") || p.getAuthority().equals("USER"))) {
            Video video = videoService.getVideoById(id);
            if (video != null) {
                String videoId = videoService.extractVideoId(video.getLink());
                if (videoId != null) {
                    try {
                        YoutubeVideoDetails videoDetails = youtubeService.getVideoDetails(videoId);
                        return new ResponseEntity<>(videoDetails, HttpStatus.OK);
                    } catch (YoutubeApiException e) {
                        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/{id}/youtube/info")
    public ResponseEntity<?> getYoutubeVideoInfo(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN") || p.getAuthority().equals("USER"))) {
            Video video = videoService.getVideoById(id);
            if (video != null) {
                String videoId = videoService.extractVideoId(video.getLink());
                if (videoId != null) {
                    try {
                        YoutubeVideoDetails videoDetails = youtubeService.getVideoDetails(videoId);
                        if (videoDetails.getItems() != null && !videoDetails.getItems().isEmpty()) {
                            var item = videoDetails.getItems().get(0);
                            var info = new VideoInfo(
                                item.getSnippet().getTitle(),
                                item.getSnippet().getDescription()
                            );
                            return new ResponseEntity<>(info, HttpStatus.OK);
                        }
                        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                    } catch (YoutubeApiException e) {
                        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping
    public ResponseEntity<Video> createVideo(@RequestBody Video video) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
            try {
                if (video.getCurso() == null || video.getCurso().getId() == null) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                String videoId = videoService.extractVideoId(video.getLink());
                if (videoId == null) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                YoutubeVideoDetails videoDetails = youtubeService.getVideoDetails(videoId);
                if (videoDetails.getItems() != null && !videoDetails.getItems().isEmpty()) {
                    video.setNombre(videoDetails.getItems().get(0).getSnippet().getTitle());
                    video.setDescripcion(videoDetails.getItems().get(0).getSnippet().getDescription());
                    video.setLink(videoService.buildYoutubeUrl(videoId));
                }
                Video nuevoVideo = videoService.createVideo(video);
                return new ResponseEntity<>(nuevoVideo, HttpStatus.CREATED);
            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } catch (YoutubeApiException e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVideo(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
            videoService.deleteVideo(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    // Clase interna para la respuesta
    private static class VideoInfo {
        private final String title;
        private final String description;

        public VideoInfo(String title, String description) {
            this.title = title;
            this.description = description;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }
    }
}
