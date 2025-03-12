package com.xmas.greet.servicio;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmas.greet.modelo.PEDIDO;
import com.xmas.greet.repositorio.PEDIDORepository;


@Service
public class PEDIDOService {
    
    @Autowired 
    private PEDIDORepository PEDIDORepository;
    
    public List<PEDIDO> listarTodas() {
        return PEDIDORepository.findAll();
    }
    
    public PEDIDO guardar(PEDIDO PEDIDO) {
        return PEDIDORepository.save(PEDIDO);
    }
    
    public PEDIDO obtenerPorId(Long id_pedido) {
        return PEDIDORepository.findById(id_pedido).orElse(null);
    }

    public List<PEDIDO> listPEDIDO() {
        return PEDIDORepository.findAll();
    }
    
}
