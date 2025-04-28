package com.Tisj.exceptions;

public class YoutubeApiException extends Exception {
    public YoutubeApiException(String message) {
        super(message);
    }

    public YoutubeApiException(String message, Throwable cause) {
        super(message, cause);
    }
} 