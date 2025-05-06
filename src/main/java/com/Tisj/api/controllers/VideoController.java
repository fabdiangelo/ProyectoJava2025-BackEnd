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
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
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
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
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


    @PostMapping
    public ResponseEntity<Video> createVideo(@RequestBody Video video) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
            try {
                String videoId = videoService.extractVideoId(video.getLink());
                if (videoId == null) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Or a more specific error
                }
                YoutubeVideoDetails videoDetails = youtubeService.getVideoDetails(videoId);
                if (videoDetails.getItems() != null && !videoDetails.getItems().isEmpty()) {
                    video.setNombre(videoDetails.getItems().get(0).getSnippet().getTitle());
                    video.setDescripcion(videoDetails.getItems().get(0).getSnippet().getDescription());
                    // Assuming you have a way to get duration from YoutubeVideoDetails
                    // video.setDuracion(...);
                }
                Video nuevoVideo = videoService.createVideo(video);
                return new ResponseEntity<>(nuevoVideo, HttpStatus.CREATED);
            } catch (YoutubeApiException e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Or a more specific error
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
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
}
