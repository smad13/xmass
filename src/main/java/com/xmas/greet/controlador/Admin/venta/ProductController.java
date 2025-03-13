package com.xmas.greet.controlador.Admin.venta;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.xmas.greet.modelo.venta.Category;
import com.xmas.greet.modelo.venta.Product;
import com.xmas.greet.servicio.venta.CategoryService;
import com.xmas.greet.servicio.venta.ProductService;

@Controller
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/form")
    public String mostrarFormularioProducto(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.listarCategorias());
        return "dashboard/Product-form";
    }

    @GetMapping("/edit/{product_id}")
    public String mostrarFormularioEdicion(@PathVariable("product_id") String productId, Model model) {
        Optional<Product> productOptional = productService.getProductById(productId);
        if (productOptional.isEmpty()) {
            return "redirect:/product/all";
        }
        model.addAttribute("product", productOptional.get());
        model.addAttribute("categories", categoryService.listarCategorias());
        return "dashboard/Product-form";
    }

    @GetMapping("/delete/{id}")
    public String eliminarProducto(@PathVariable String id, RedirectAttributes redirectAttributes) {
        productService.deleteProduct(id);
        return "redirect:/product/all";
    }

    @GetMapping("/all")
    public String listarProductos(Model model) {
        List<Product> products = productService.getAllProducts();
        for (Product product : products) {
            if (product.getImagen() != null) {
                String base64Image = Base64.getEncoder().encodeToString(product.getImagen());
                product.setBase64Image(base64Image);
            }
        }
        model.addAttribute("products", products);
        return "dashboard/Product-list";
    }

    @PostMapping("/create")
    public String createProduct(
        @RequestParam String name,
        @RequestParam Double price,
        @RequestParam String description,
        @RequestParam Long categoryId,
        @RequestParam int stock,
        @RequestParam(required = false) MultipartFile imageFile,
        RedirectAttributes redirectAttributes
    ) {
        Optional<Category> categoryOptional = Optional.ofNullable(categoryService.obtenerCategoriaPorId(categoryId));
        if (categoryOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("Mensaje_Error", "Categoría no encontrada");
            return "redirect:/product/all";
        }
    
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setDescription(description);
        product.setCategoria(categoryOptional.get());
        product.setStock(stock);
    
        try {
            productService.saveProduct(product, imageFile);
            redirectAttributes.addFlashAttribute("Mensaje_Guardado", "El producto fue guardado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("Mensaje_Error", "Error al guardar el producto");
        }
    
        return "redirect:/product/all";
    }
    @PostMapping("/update/{id}")
    public String updateProduct(
        @PathVariable("id") String id,
        @RequestParam String name,
        @RequestParam Double price,
        @RequestParam String description,
        @RequestParam Long categoryId,
        @RequestParam int stock,
        @RequestParam(required = false) MultipartFile imageFile,
        RedirectAttributes redirectAttributes
    ) {
        // Se obtiene el producto a actualizar
        Optional<Product> productOptional = productService.getProductById(id);
        if (productOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("Mensaje_Error", "Producto no encontrado");
            return "redirect:/product/all";
        }
        Product product = productOptional.get();
    
        // Se actualizan los campos
        product.setName(name);
        product.setPrice(price);
        product.setDescription(description);
        product.setStock(stock);
    
        Optional<Category> categoryOptional = Optional.ofNullable(categoryService.obtenerCategoriaPorId(categoryId));
        if (categoryOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("Mensaje_Error", "Categoría no encontrada");
            return "redirect:/product/all";
        }
        product.setCategoria(categoryOptional.get());
    
        try {
            // Se actualiza el producto. Si se envía una nueva imagen, se actualiza; de lo contrario, se mantiene la existente.
            productService.saveProduct(product, imageFile);
            redirectAttributes.addFlashAttribute("Mensaje_Guardado", "Producto actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("Mensaje_Error", "Error al actualizar el producto");
        }
        return "redirect:/product/all";
    }
    
}