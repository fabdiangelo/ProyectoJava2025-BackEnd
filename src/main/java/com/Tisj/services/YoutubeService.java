package com.Tisj.services;

import com.Tisj.api.response.YoutubeVideoDetails;
import com.Tisj.exceptions.YoutubeApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class YoutubeService {

    private static final Logger logger = LoggerFactory.getLogger(YoutubeService.class);

    private final RestTemplate restTemplate;
    private final String apiKey;

    public YoutubeService(@Value("${youtube.api.key}") String apiKey) {
        this.restTemplate = new RestTemplate();
        this.apiKey = apiKey;
    }

    public YoutubeVideoDetails getVideoDetails(String videoId) throws YoutubeApiException {
        validateVideoId(videoId);
        String url = buildYoutubeApiUrl(videoId);

        try {
            logger.debug("Calling YouTube API with URL: {}", url);
            ResponseEntity<YoutubeVideoDetails> response = restTemplate.getForEntity(url, YoutubeVideoDetails.class);

            if (response.getBody() == null) {
                throw new YoutubeApiException("No se encontró información para el video con ID: " + videoId);
            }

            return response.getBody();
        } catch (HttpClientErrorException e) {
            logger.error("YouTube API returned an error: {}", e.getStatusCode());
            throw new YoutubeApiException("Error al obtener información del video: " + e.getStatusCode());
        } catch (Exception e) {
            logger.error("Unexpected error while fetching video details: {}", e.getMessage(), e);
            throw new YoutubeApiException("Error inesperado al obtener información del video");
        }
    }

    private void validateVideoId(String videoId) {
        if (videoId == null || videoId.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del video no puede ser nulo o vacío");
        }
    }

    private String buildYoutubeApiUrl(String videoId) {
        return String.format(
                "https://www.googleapis.com/youtube/v3/videos?part=snippet,statistics&id=%s&key=%s",
                videoId,
                apiKey
        );
    }
}
