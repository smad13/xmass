package com.xmas.greet.repositorio.venta;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xmas.greet.modelo.venta.Product;

public interface ProducRepository extends JpaRepository<Product,String >{

   
    List<Product> findFirst4ByOrderByPriceAsc();
    void deleteById(String Id);
    Optional<Product> findById(String id);
    List<Product> findByNameContainingIgnoreCase(String name);


    
}
