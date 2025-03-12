package com.xmas.greet.repositorio.venta;



import org.springframework.data.jpa.repository.JpaRepository;

import com.xmas.greet.modelo.venta.Category;


public interface CategoryRepository extends JpaRepository <Category, Long> {
    
}
