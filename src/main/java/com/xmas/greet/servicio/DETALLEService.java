package com.xmas.greet.servicio;

import com.xmas.greet.modelo.DETALLE;
import com.xmas.greet.repositorio.DETALLERepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DETALLEService {
    @Autowired
    private DETALLERepository DETALLERepository;
    
    public List<DETALLE> listarTodas() {
        return DETALLERepository.findAll();
    }
    
    public DETALLE guardar(DETALLE DETALLE) {
        return DETALLERepository.save(DETALLE);
    }
    
    public DETALLE obtenerPorId(Long id_detalle) {
        return DETALLERepository.findById(id_detalle).orElse(null);
    }

    public List<DETALLE> listDETALLE() {
     return DETALLERepository.findAll();
    }
}
