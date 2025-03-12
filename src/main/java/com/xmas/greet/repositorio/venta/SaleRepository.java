package com.xmas.greet.repositorio.venta;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xmas.greet.modelo.venta.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    List<Sale> findByUsuario_Correo(String correo);
    boolean existsById(Long id); 
}
