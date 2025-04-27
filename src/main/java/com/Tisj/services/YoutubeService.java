package com.Tisj.services;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

@Service
public class YoutubeService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String getVideoDetails(String videoId) {
        String API_KEY = "${YOUTUBE_API_KEY}";
        String url = "https://www.googleapis.com/youtube/v3/videos"
                + "?part=snippet,statistics"
                + "&id=" + videoId
                + "&key=" + API_KEY;

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }
}
