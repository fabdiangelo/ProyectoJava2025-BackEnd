package com.Tisj.api.controllers;

import com.Tisj.api.response.YoutubeVideoDetails;
import com.Tisj.exceptions.YoutubeApiException;
import com.Tisj.services.YoutubeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/youtube")
public class YoutubeController {

    private static final Logger logger = LoggerFactory.getLogger(YoutubeController.class);
    private final YoutubeService youtubeService;

    public YoutubeController(YoutubeService youtubeService) {
        this.youtubeService = youtubeService;
    }

    @Operation(
        summary = "Obtener detalles de un video de YouTube",
        description = "Retorna título, descripción y estadísticas de un video público de YouTube."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Video encontrado exitosamente"),
        @ApiResponse(responseCode = "400", description = "ID de video inválido"),
        @ApiResponse(responseCode = "404", description = "Video no encontrado"),
        @ApiResponse(responseCode = "403", description = "No autorizado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/video")
    public ResponseEntity<?> getVideo(
            @Parameter(description = "ID del video de YouTube", required = true)
            @RequestParam String id) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN") || p.getAuthority().equals("USER"))) {
            
            logger.debug("Solicitud para obtener detalles del video con ID: {}", id);

            try {
                YoutubeVideoDetails videoDetails = youtubeService.getVideoDetails(id);
                if (videoDetails.getItems() != null && !videoDetails.getItems().isEmpty()) {
                    return ResponseEntity.ok(videoDetails.getItems().get(0));
                } else {
                    return ResponseEntity.notFound().build();
                }
            } catch (IllegalArgumentException e) {
                logger.warn("Solicitud inválida: {}", e.getMessage());
                return ResponseEntity.badRequest().body(e.getMessage());
            } catch (YoutubeApiException e) {
                logger.error("Error al consultar YouTube: {}", e.getMessage());
                return ResponseEntity.internalServerError().body(e.getMessage());
            } catch (Exception e) {
                logger.error("Error inesperado: {}", e.getMessage(), e);
                return ResponseEntity.internalServerError().body("Ocurrió un error inesperado.");
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}


