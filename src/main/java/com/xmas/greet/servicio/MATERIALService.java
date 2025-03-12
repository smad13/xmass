package com.xmas.greet.servicio;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmas.greet.modelo.MATERIAL;
import com.xmas.greet.repositorio.MATERIALRepository;
@Service
public class MATERIALService {

    @Autowired
    private MATERIALRepository MATERIALRepository;

    public List<MATERIAL> listarTodas() {
        return MATERIALRepository.findAll();
    }
    
    public MATERIAL guardar(MATERIAL MATERIAL) {
        return MATERIALRepository.save(MATERIAL);
    }
    
    public MATERIAL obtenerPorId(Long ID_materiales) {
        return MATERIALRepository.findById(ID_materiales).orElse(null);
    }
    
    public void eliminar(Long ID_materiales) {
        MATERIALRepository.deleteById(ID_materiales);
    }
    public List<MATERIAL> listMATERIAL() {
        throw new UnsupportedOperationException("Unimplemented method 'listMATERIAL'");
    }

}