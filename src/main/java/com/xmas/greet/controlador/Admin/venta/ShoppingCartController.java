package com.xmas.greet.controlador.Admin.venta;

import com.xmas.greet.modelo.Usuario;
import com.xmas.greet.modelo.venta.Product;
import com.xmas.greet.modelo.venta.ShoppingCart;
import com.xmas.greet.servicio.UsuarioServicio;
import com.xmas.greet.servicio.venta.ProductService;
import com.xmas.greet.servicio.venta.ShoppingCartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/carrito")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    private final ProductService productService;
    private final UsuarioServicio usuarioServicio;

    public ShoppingCartController(ShoppingCartService shoppingCartService, ProductService productService, UsuarioServicio usuarioServicio) {
        this.shoppingCartService = shoppingCartService;
        this.productService = productService;
        this.usuarioServicio = usuarioServicio;
    }

    @GetMapping
    public String viewCart(Model model, Principal principal) {
        model.addAttribute("cartItems", shoppingCartService.getListByClient(principal.getName()));
        return "carrito";
    }

    @PostMapping("/agregar")
    public String addToCart(@RequestParam String productId, @RequestParam int amount, Principal principal) {
        Usuario usuario = usuarioServicio.findByCorreo(principal.getName());
        Product product = productService.getProductById(productId).orElseThrow();

        if (product.getStock() >= amount) {
            ShoppingCart item = new ShoppingCart(null, product, usuario, amount);
            shoppingCartService.addProduct(item);
        } else {
            return "redirect:/carrito?error=Stock insuficiente";
        }

        return "redirect:/carrito";
    }

    @PostMapping("/eliminar/{id}")
    public String removeFromCart(@PathVariable Long id) {
        shoppingCartService.removeProduct(id);
        return "redirect:/carrito";
    }
}
