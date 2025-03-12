package com.xmas.greet.servicio.venta;

import java.util.List;

import org.springframework.stereotype.Service;

import com.xmas.greet.modelo.venta.ShoppingCart;
import com.xmas.greet.repositorio.venta.ShoppingCartRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }
    
    public List<ShoppingCart> getListByClient(String correo){
        return this.shoppingCartRepository.findByUsuario_Correo(correo);
    }
    public void cleanShoppigCart (Long usuarioId){
        this.shoppingCartRepository.deleteByUsuario_Id(usuarioId);
    }
    public void removeProduct(Long id){
        this.shoppingCartRepository.deleteById(id);
    }
    public void addProduct(ShoppingCart shoppingCart){
        this.shoppingCartRepository.save(shoppingCart);
    }
    public Long getCountByClient(Long id){
        return this.shoppingCartRepository.countByUsuario_Id(id);
    }

}
