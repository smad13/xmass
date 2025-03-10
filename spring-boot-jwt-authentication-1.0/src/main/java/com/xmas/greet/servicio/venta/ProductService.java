package com.xmas.greet.servicio.venta;


import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.xmas.greet.modelo.venta.Product;
import com.xmas.greet.repositorio.venta.ProducRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductService {

    private final ProducRepository producRepository;
    public ProductService(ProducRepository productRepository) {
        this.producRepository = productRepository;
    }
    
    public void saveProduct(Product product){
        this.producRepository.save(product);
    }
   public List<Product> getAllProducts(){
        return this.producRepository.findAll();
   }

   public Optional<Product> getProductById(String id){
        return this.producRepository.findById(id);
   }
   public Product getProductByIdSale(String id) {
    return producRepository.findById(id).orElse(null);
}
   public List<Product> getBestPriceProducts(){
        return this.producRepository.findFirst4ByOrderByPriceAsc();
   }
   public void reduceStock(String productId, int quantity) {
    Product product = producRepository.findById(productId).orElseThrow();
    if (product.getStock() < quantity) {
        throw new RuntimeException("Stock insuficiente");
    }
    product.setStock(product.getStock() - quantity);
    producRepository.save(product);
}

public void deleteProduct (String Id){
    producRepository.deleteById(Id);
}
public List<Product> buscarPorNombre(String query) {
    return producRepository.findByNameContainingIgnoreCase(query);
}


}