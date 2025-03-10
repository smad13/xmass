package com.xmas.greet.repositorio.venta;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.xmas.greet.modelo.venta.ShoppingCart;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart,Long > {

    List<ShoppingCart> findByUsuario_Id(Long usuarioId);
    List<ShoppingCart> findByUsuario_Correo(String correo);

    void deleteById(Long id);
    void deleteByUsuario_Id(Long usuarioId);
    Long countByUsuario_Id(Long id);
 
}
