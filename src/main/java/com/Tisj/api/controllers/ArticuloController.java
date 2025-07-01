package com.Tisj.api.controllers;

import com.Tisj.api.response.PagedResponse;
import com.Tisj.bussines.entities.Articulo;
import com.Tisj.bussines.entities.DT.DTArticulo;
import com.Tisj.services.ArticuloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/articulos")
public class ArticuloController {

    @Autowired
    private ArticuloService articuloService;

    @GetMapping(value = "/paginado", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResponse<DTArticulo>> articulosPaginados(
            @RequestParam int pagina,
            @RequestParam int cantidad,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String tipos,
            @RequestParam(required = false) Float precioMin,
            @RequestParam(required = false) Float precioMax) {

        Pageable pageable = PageRequest.of(pagina, cantidad);
        List<String> tiposList = tipos != null ? Arrays.asList(tipos.split(",")) : null;

        String s = search;
        if (s == null){
            s = "";
        }
        Page<Articulo> page = articuloService.getFiltrados(s, tiposList, precioMin, precioMax, pageable);
        List<DTArticulo> dtos = page.getContent()
                .stream()
                .map(art -> new DTArticulo(art))
                .toList();

        PagedResponse<DTArticulo> response = new PagedResponse<>(
                dtos,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );

        java.lang.System.out.println("Cantidad resultados: " + page.getContent().size());

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/max-precio")
    public ResponseEntity<Float> articulosPaginados() {
        Articulo articulo = articuloService.getMaxPrice();
        if (articulo != null) {
            return ResponseEntity.ok(articulo.getPrecio());
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    @PutMapping("/{id}")
    public ResponseEntity<Articulo> updateArticulo(@PathVariable Long id, @RequestBody Articulo articulo) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN") || p.getAuthority().equals("USER"))) {
            Articulo articuloActualizado = articuloService.updateArticulo(id, articulo);
            if (articuloActualizado != null) {
                return new ResponseEntity<>(articuloActualizado, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticulo(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN") || p.getAuthority().equals("USER"))) {
            articuloService.deleteArticulo(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

}
