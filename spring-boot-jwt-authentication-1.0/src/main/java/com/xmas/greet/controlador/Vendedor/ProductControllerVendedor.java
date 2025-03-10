package com.xmas.greet.controlador.Vendedor;

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
@RequestMapping("/vendedor/product")
public class ProductControllerVendedor {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductControllerVendedor(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/form")
    public String mostrarFormularioProducto(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.listarCategorias());
        return "dashboard-vendedor/Product-form";
    }

    @GetMapping("/edit/{product_id}")
    public String mostrarFormularioEdicion(@PathVariable("product_id") String productId, Model model) {
        Optional<Product> productOptional = productService.getProductById(productId);
        if (productOptional.isEmpty()) {
            return "redirect:/vendedor/product/all";
        }
        model.addAttribute("product", productOptional.get());
        model.addAttribute("categories", categoryService.listarCategorias());
        return "dashboard-vendedor/Product-form";
    }

    
    @GetMapping("/delete/{id}")
    public String eliminarProducto(@PathVariable String id, RedirectAttributes redirectAttributes) {
        productService.deleteProduct(id);
        return "redirect:/vendedor/product/all";
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
        return "dashboard-vendedor/Product-list";
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
        try {
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
            if (imageFile != null && !imageFile.isEmpty()) {
                product.setImagen(imageFile.getBytes());
            }
            productService.saveProduct(product);
            redirectAttributes.addFlashAttribute("Mensaje_Guardado", "El producto fue guardado correctamente");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("Mensaje_Error", "Error al guardar el producto");
        }
        return "redirect:/vendedor/product/all";
    }

    @PostMapping("/update/{product_id}")
    public String updateProduct(
        @PathVariable("product_id") String productId,
        @RequestParam String name,
        @RequestParam Double price,
        @RequestParam String description,
        @RequestParam Long categoryId,
        @RequestParam int stock,
        @RequestParam(required = false) MultipartFile imageFile,
        RedirectAttributes redirectAttributes
    ) {
        Optional<Product> existingProduct = productService.getProductById(productId);
        if (existingProduct.isEmpty()) {
            redirectAttributes.addFlashAttribute("Mensaje_NotFound", "Producto no encontrado");
            return "redirect:/product/all";
        }
        Optional<Category> categoryOptional = Optional.ofNullable(categoryService.obtenerCategoriaPorId(categoryId));
        if (categoryOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("Mensaje_Error", "Categoría no encontrada");
            return "redirect:/vendedor/product/all";
        }
        Product product = existingProduct.get();
        product.setName(name);
        product.setPrice(price);
        product.setDescription(description);
        product.setCategoria(categoryOptional.get());
        product.setStock(stock);
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                product.setImagen(imageFile.getBytes());
            }
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("Actualiza_Error", "Error al actualizar producto");
        }
        productService.saveProduct(product);
        redirectAttributes.addFlashAttribute("Actualizar_producto", "Se guardó correctamente");
        return "redirect:/vendedor/product/all";
    }




}
