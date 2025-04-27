package com.Tisj.api.controllers;

import com.Tisj.services.YoutubeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class YoutubeController {

    private final YoutubeService youtubeService;

    public YoutubeController(YoutubeService youtubeService) {
        this.youtubeService = youtubeService;
    }

    @GetMapping("/video")
    public String getVideo(@RequestParam String id) {
        return youtubeService.getVideoDetails(id);
    }
}

