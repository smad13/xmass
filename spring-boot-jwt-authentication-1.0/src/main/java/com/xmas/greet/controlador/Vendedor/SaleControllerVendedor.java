package com.xmas.greet.controlador.Vendedor;


import com.xmas.greet.modelo.Usuario;
import com.xmas.greet.modelo.venta.Detail;
import com.xmas.greet.modelo.venta.Product;
import com.xmas.greet.modelo.venta.Sale;
import com.xmas.greet.repositorio.venta.SaleRepository;
import com.xmas.greet.servicio.UsuarioServicio;
import com.xmas.greet.servicio.venta.DetailService;
import com.xmas.greet.servicio.venta.ProductService;
import com.xmas.greet.servicio.venta.SaleService;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/vendedor/ventas")
public class SaleControllerVendedor {

    private final SaleService saleService;
    private final DetailService detailService;
    private final SaleRepository saleRepository;
    private final ProductService productService;
    private final UsuarioServicio usuarioServicio;


    

    public SaleControllerVendedor(SaleService saleService, DetailService detailService, SaleRepository saleRepository,
            ProductService productService, UsuarioServicio usuarioServicio) {
        this.saleService = saleService;
        this.detailService = detailService;
        this.saleRepository = saleRepository;
        this.productService = productService;
        this.usuarioServicio = usuarioServicio;
    }

    // **Mostrar formulario de ventas**
    @GetMapping("/nuevo")
    public String mostrarFormularioVenta(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login?error=Debe iniciar sesión";
        }
        List<Product> products = productService.getAllProducts();
		for (Product product : products) {
			if (product.getImagen() != null) {
				String base64Image = Base64.getEncoder().encodeToString(product.getImagen());
				product.setBase64Image(base64Image);
			}
		}
		model.addAttribute("products", products);
        model.addAttribute("usuarioCorreo", principal.getName());
        return "dashboard-vendedor/ventas-form";
    }

    // **Completar compra (procesar el formulario)**
    @PostMapping("/crear")
public ResponseEntity<?> crearVenta(@RequestBody Map<String, Object> venta) {
    System.out.println("Venta recibida: " + venta);

    // Validar datos
    if (!venta.containsKey("cliente") || !venta.containsKey("fecha") || !venta.containsKey("productos")) {
        return ResponseEntity.badRequest().body("Faltan datos en la venta.");
    }

    String correoCliente = (String) venta.get("cliente");
    List<Map<String, Object>> productos = (List<Map<String, Object>>) venta.get("productos");

    try {
        // Crear la venta
        Usuario usuario = usuarioServicio.findByCorreo(correoCliente);
        if (usuario == null) {
            return ResponseEntity.badRequest().body("Cliente no encontrado.");
        }

        double total = productos.stream()
            .mapToDouble(p -> Double.parseDouble(p.get("precio").toString()) * Integer.parseInt(p.get("cantidad").toString()))
            .sum();

        Sale sale = new Sale(total, new Date(), usuario);
        sale = saleRepository.save(sale); // Guardar la venta

        // Guardar detalles de la venta
        for (Map<String, Object> producto : productos) {
            String productId = (String) producto.get("id"); // ID en String
            int cantidad = Integer.parseInt(producto.get("cantidad").toString());

            Product product = productService.getProductByIdSale(productId);
            if (product == null) {
                return ResponseEntity.badRequest().body("Producto no encontrado: " + productId);
            }

            if (product.getStock() < cantidad) {
                return ResponseEntity.badRequest().body("Stock insuficiente para el producto: " + product.getName());
            }

            Detail detail = new Detail( product, sale, cantidad);
            detailService.createDetail(detail);

            // Reducir stock del producto
            productService.reduceStock(productId, cantidad);
        }

        return ResponseEntity.ok(Collections.singletonMap("mensaje", "Venta creada exitosamente"));

    } catch (Exception e) {
        return ResponseEntity.badRequest().body("Error al crear la venta: " + e.getMessage());
    }
}


    // **Mostrar detalles de una venta**
    @GetMapping("/{saleId}")
    public String verDetalleVenta(@PathVariable Long saleId, Model model) {
        Optional<Sale> saleOptional = saleService.getSaleById(saleId);
        
        if (saleOptional.isEmpty()) {
            return "redirect:/ventas?error=Venta no encontrada";
        }
    
        Sale sale = saleOptional.get();
        
        // Obtener los detalles de la venta
        List<Detail> detalles = detailService.getDetailbySale(saleId);
        
        // Convertir las imágenes de los productos en cada detalle a Base64
        for (Detail detalle : detalles) {
            Product product = detalle.getProduct(); // Cada detalle tiene un producto asociado
            if (product != null && product.getImagen() != null) {
                String base64Image = Base64.getEncoder().encodeToString(product.getImagen());
                product.setBase64Image(base64Image); // Asignamos el valor convertido
            }
        }
        
        model.addAttribute("sale", sale);
        model.addAttribute("details", detalles);
    
        return "dashboard-vendedor/venta-detalle"; 
    }
    
    // **Listar todas las ventas**
    @GetMapping
    public String listarVentas(Model model) {
        model.addAttribute("sales", saleService.getAllSales());
        return "dashboard-vendedor/ventas-list";
    }


    
}
