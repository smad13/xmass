package com.xmas.greet.servicio.venta;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.xmas.greet.modelo.Usuario;
import com.xmas.greet.modelo.venta.Detail;
import com.xmas.greet.modelo.venta.Product;
import com.xmas.greet.modelo.venta.Sale;
import com.xmas.greet.repositorio.venta.ProducRepository;
import com.xmas.greet.repositorio.venta.SaleRepository;
import com.xmas.greet.servicio.UsuarioServicio;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class SaleService {

    private SaleRepository saleRepository;
    private UsuarioServicio usuarioServicio;
    private ShoppingCartService shoppingCartService;
    private ProducRepository producRepository;
    private DetailService detailService;
    private ProductService productService;




    public SaleService(SaleRepository saleRepository, UsuarioServicio usuarioServicio,
            ShoppingCartService shoppingCartService, DetailService detailService, ProductService productService) {
        this.saleRepository = saleRepository;
        this.usuarioServicio = usuarioServicio;
        this.shoppingCartService = shoppingCartService;
        this.detailService = detailService;
        this.productService = productService;
    }


    public List<Sale> getSalesByClient(String correo){
        return this.saleRepository.findByUsuario_Correo(correo);
    }


    public List<Sale> getAllSales() {
        List<Sale> sales = saleRepository.findAll();
        return sales != null ? sales : List.of();
    }

    public Optional<Sale> getSaleById(Long Id){
        return saleRepository.findById(Id);
    }



    public void createSale(String correo, List<Map<String, Object>> productos) {
        Usuario usuario = usuarioServicio.findByCorreo(correo);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado.");
        }
    
        double total = productos.stream()
            .mapToDouble(p -> Double.parseDouble(p.get("precio").toString()) * Integer.parseInt(p.get("cantidad").toString()))
            .sum();
    
        Sale sale = new Sale(total, new Date(), usuario);
        sale = saleRepository.save(sale); // Guardar la venta
    
        for (Map<String, Object> producto : productos) {
            String productId = (String) producto.get("id");
            int cantidad = Integer.parseInt(producto.get("cantidad").toString());
    
            Product product = productService.getProductByIdSale(productId);
            if (product == null) {
                throw new RuntimeException("Producto no encontrado: " + productId);
            }
    
            if (product.getStock() < cantidad) {
                throw new RuntimeException("Stock insuficiente para el producto: " + product.getName());
            }
    
            Detail detail = new Detail(product, sale, cantidad);
            detailService.createDetail(detail);
    
            productService.reduceStock(productId, cantidad);
        }
    }
    public Product getProductById(String id) {
        return producRepository.findById(id).orElse(null);
    }
    
    
    
    
    
} 
