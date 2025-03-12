package com.xmas.greet.controlador.Admin;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xmas.greet.modelo.DETALLE;
import com.xmas.greet.modelo.PEDIDO;
import com.xmas.greet.modelo.Usuario;
import com.xmas.greet.modelo.venta.Product;
import com.xmas.greet.servicio.DETALLEService;
import com.xmas.greet.servicio.PEDIDOService;
import com.xmas.greet.servicio.UsuarioServicio;
import com.xmas.greet.servicio.venta.ProductService;

import jakarta.transaction.Transactional;

@Controller
public class Controlador {

	@Autowired
	private UsuarioServicio usuarioServicio;
	private ProductService productService;
	private DETALLEService detalleService;
	private PEDIDOService pedidoService;	


	

    


	public Controlador(UsuarioServicio usuarioServicio, ProductService productService, DETALLEService detalleService,
            PEDIDOService pedidoService) {
        this.usuarioServicio = usuarioServicio;
        this.productService = productService;
        this.detalleService = detalleService;
        this.pedidoService = pedidoService;
    }


    @GetMapping("/login")
	public String iniciarSesion() {
		return "login";
	}

	
	@GetMapping("/admin/dashboard")
	public String verPaginaDeInicio(Model model) {
		return "dashboard/index";

	}

	@GetMapping("/clientes")
	public String obtenerClientes(Model model) {
        List<Usuario> usuarios = usuarioServicio.listarUsuarios();
  List<Usuario> clientes = usuarios.stream()
            .filter(usuario -> usuario.getRoles().stream()
                .anyMatch(rol -> rol.getNombre().equals("ROLE_CLIENTE")))
            .collect(Collectors.toList());
			model.addAttribute("clientes", clientes);
		return "dashboard/clientes";
	}

	@GetMapping("/empleados")
	public String listarEmpleados(Model model) {
		List<Usuario> usuarios = usuarioServicio.obtenerTodosLosUsuarios();
	
		// Filtrar empleados y eliminar duplicados
		List<Usuario> empleados = usuarios.stream()
			.filter(usuario -> usuario.getRoles().stream()
				.anyMatch(rol -> rol.getNombre().equals("ROLE_VENDEDOR") || rol.getNombre().equals("ROLE_ADMIN")))
			// Evitar duplicados
			.distinct() 
			.collect(Collectors.toList());
	
		// Convertir nombres de roles a etiquetas legibles
		empleados.forEach(usuario -> {
			usuario.setRoles(usuario.getRoles().stream()
			
				.distinct() 
				.map(rol -> {
					if (rol.getNombre().equals("ROLE_ADMIN")) rol.setNombre("Administrador");
					else if (rol.getNombre().equals("ROLE_VENDEDOR")) rol.setNombre("Vendedor");
					return rol;
				})
				.collect(Collectors.toList()));
		});
	
		model.addAttribute("empleados", empleados);
		return "dashboard/empleados";
	}
	
	

	@GetMapping("/index")
	public String inicio() {
		return "inicio/index";
	}

	@GetMapping("/REPORTES")
    public String mostrarReportes() {
        return "dashboard/REPORTES";
    }



	@GetMapping("/vendedor/dashboard-clientes")
    public String clientes() {
        return "dashboard-vendedor/clientes";
    }
	@GetMapping("/recuperar")
    public String recuperar() {
        return "recuperar";
    }

	
	@GetMapping("/catalogo")
	public String mostrarCatalogo(Model model) {
		List<Product> products = productService.getAllProducts();
	
		// Convertir imagen a Base64 usando parallel stream
		products.parallelStream().forEach(product -> {
			if (product.getImagen() != null) {
				String base64Image = Base64.getEncoder().encodeToString(product.getImagen());
				product.setBase64Image(base64Image);
			}
		});
	
		// Agrupar productos por categoría
		Map<String, List<Product>> productosPorCategoria = products.stream()
        .filter(product -> product.getCategoria() != null && product.getCategoria().getNombre() != null) 
        .collect(Collectors.groupingBy(product -> product.getCategoria().getNombre()));
	
		model.addAttribute("productosPorCategoria", productosPorCategoria);
		return "inicio/catalogo";
	}

	@GetMapping("/catalogo/{product_id}")
public String informacionById(@PathVariable("product_id") String productId, Model model) {
    Optional<Product> productOptional = productService.getProductById(productId);

    if (productOptional.isEmpty()) {
        return "redirect:/catalogo"; // Redirige al catálogo si el producto no existe
    }

    Product product = productOptional.get();

    // Convertir la imagen del producto a Base64
    if (product.getImagen() != null) {
        String base64Image = Base64.getEncoder().encodeToString(product.getImagen());
        product.setBase64Image(base64Image);
    }

    model.addAttribute("product", product);
    return "inicio/Product-detalle";
}

@GetMapping("/carrito-pedido")
public String formPedido( Model model) {
	List<Product> products = productService.getAllProducts();
	// Convertir imagen a Base64 usando parallel stream
	products.parallelStream().forEach(product -> {
		if (product.getImagen() != null) {
			String base64Image = Base64.getEncoder().encodeToString(product.getImagen());
			product.setBase64Image(base64Image);
		}
	});
	model.addAttribute("products", products);
	return "inicio/carrito-pedido";
}

    @PostMapping("/carrito-pedido")
    @ResponseBody
    @Transactional
    public ResponseEntity<?> guardarPedidoCompleto(@RequestBody Map<String, Object> payload) {
        ObjectMapper mapper = new ObjectMapper();
        
        // Extraer el correo del payload
        String correo = (String) payload.get("correo");
        if (correo == null || correo.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El correo es obligatorio.");
        }
        
        // Buscar el usuario por correo
        Usuario usuario = usuarioServicio.obtenerPorCorreo(correo);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("El usuario con correo " + correo + " no existe.");
        }
        
        // Convertir la parte "pedido" del JSON a un objeto PEDIDO
        PEDIDO pedido = mapper.convertValue(payload.get("pedido"), PEDIDO.class);
        // Asignar el id_cliente obtenido del usuario encontrado
        pedido.setId_cliente(usuario.getId().intValue());
        
        // Convertir la parte "detalles" del JSON a una lista de DETALLE
        List<DETALLE> detalles = mapper.convertValue(payload.get("detalles"), new TypeReference<List<DETALLE>>() {});
        
        // Validar que se envíe al menos un detalle
        if (detalles == null || detalles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se proporcionaron detalles del pedido.");
        }
        
        // Validar cada detalle: cantidad y precio deben ser mayores a cero y el producto debe existir
        for (DETALLE detalle : detalles) {
            if (detalle.getCantidad() == null || detalle.getCantidad() <= 0 ||
                detalle.getPrecio_unitario() == null || detalle.getPrecio_unitario() <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("La cantidad y el precio_unitario deben ser mayores a cero para el producto: " + detalle.getId_producto());
            }
            Optional<Product> prodOptional = productService.getProductById(detalle.getId_producto());
            if (prodOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El producto con ID " + detalle.getId_producto() + " no existe.");
            }
            // Calcular subtotal y asignar la fecha de registro
            detalle.setSubtotal(BigDecimal.valueOf(detalle.getCantidad())
                    .multiply(BigDecimal.valueOf(detalle.getPrecio_unitario())));
            detalle.setFecha_registro(LocalDateTime.now());
        }
        
        try {
            // Guardar el pedido
            PEDIDO pedidoGuardado = pedidoService.guardar(pedido);
            // Asignar el id del pedido a cada detalle y guardarlos
            for (DETALLE detalle : detalles) {
                detalle.setId_pedido(pedidoGuardado.getId_pedido().intValue());
                detalleService.guardar(detalle);
            }
            return ResponseEntity.ok("Pedido guardado correctamente con ID: " + pedidoGuardado.getId_pedido());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar el pedido y sus detalles: " + e.getMessage());
        }
    }



}
