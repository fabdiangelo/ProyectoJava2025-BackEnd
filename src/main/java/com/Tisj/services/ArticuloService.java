package com.Tisj.services;

import com.Tisj.bussines.entities.Articulo;
import com.Tisj.bussines.entities.Curso;
import com.Tisj.bussines.entities.DT.DTArticulo;
import com.Tisj.bussines.entities.Paquete;
import com.Tisj.bussines.repositories.ArticuloRepository;
import com.Tisj.bussines.repositories.CursoRepository;
import com.Tisj.bussines.repositories.PaqueteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class ArticuloService {

    @Autowired
    private ArticuloRepository articuloRepository;

    @Autowired
    private PaqueteRepository paqueteRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<Articulo> getAllArticulos() {
        List<Articulo> articulos = articuloRepository.findAll();
        articulos.forEach(a -> {
            if (a.getOferta() != null) {
                a.getOferta().getArticulos().size(); // Forzar la inicialización
            }
        });
        return articulos;
    }

    @Transactional(readOnly = true)
    public Articulo getArticuloById(Long id) {
        Articulo articulo = articuloRepository.findById(id).orElse(null);
        if (articulo != null && articulo.getOferta() != null) {
            articulo.getOferta().getArticulos().size(); // Forzar la inicialización
        }
        return articulo;
    }

    public Articulo createArticulo(Articulo articulo) {
        return articuloRepository.save(articulo);
    }

    public Articulo updateArticulo(Long id, Articulo articulo) {
        Articulo articuloExistente = articuloRepository.findById(id).orElse(null);
        if (articuloExistente != null) {
            articulo.setId(articuloExistente.getId());
            return articuloRepository.save(articulo);
        }
        return null;
    }

    public void deleteArticulo(Long id) {
        articuloRepository.deleteById(id);
    }

    public Page<Articulo> listadoArticuloPaginado(Integer pagina, Integer cantidad) {
        PageRequest pageRequest = PageRequest.of(pagina, cantidad);
        return articuloRepository.findAll(pageRequest);
    }

//    public Page<Articulo> getFiltrados(String search, List<String> tipos, Pageable pageable) {
//        List<Articulo> resultados = new ArrayList<>();
//
//        if (tipos != null){
//            if (tipos.contains("curso")) {
//                Page<Curso> cursos = cursoRepository.buscarCursosFiltrados(String.valueOf(search), pageable);
//                resultados.addAll(cursos.stream().toList());
//            }
//
//            if (tipos.contains("paquete")) {
//                Page<Paquete> paquetes = paqueteRepository.buscarPaquetesFiltrados(String.valueOf(search), pageable);
//                resultados.addAll(paquetes.stream().toList());
//            }
//        }
//
//
//        List<Articulo> pagina = resultados.stream()
//                .skip((long) pageable.getPageNumber() * pageable.getPageSize())
//                .limit(pageable.getPageSize())
//                .toList();
//
//        return new PageImpl<>(pagina, pageable, resultados.size());
//    }

    public Page<Articulo> getFiltrados(String search, List<String> tipos, Float precioMin, Float precioMax, Pageable pageable) {
        boolean incluyeCurso = tipos != null && tipos.contains("curso");
        boolean incluyePaquete = tipos != null && tipos.contains("paquete");
        boolean tiposNoNulo = tipos != null;

        return articuloRepository.buscarFiltrados(
                String.valueOf(search),
                incluyeCurso,
                incluyePaquete,
                tiposNoNulo,
                precioMin,
                precioMax,
                pageable
        );
    }


    public Articulo getMaxPrice() {
        return articuloRepository.findTopByOrderByPrecioDesc();
    }
}
