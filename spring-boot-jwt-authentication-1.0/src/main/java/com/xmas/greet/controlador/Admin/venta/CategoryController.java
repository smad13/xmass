package com.xmas.greet.controlador.Admin.venta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xmas.greet.modelo.venta.Category;
import com.xmas.greet.servicio.venta.CategoryService;

@Controller
@RequestMapping("/Category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }



    @GetMapping
    public String listarCategorias(Model model) {
        model.addAttribute("categorias", categoryService.listarCategorias());
        return "dashboard/categorias-list";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevaCategoria(Model model) {
        model.addAttribute("categoria", new Category());
        return "dashboard/categorias-form";
    }

    @PostMapping("/guardar")
    public String guardarCategoria(@ModelAttribute Category category) {
        categoryService.guardarCategoria(category);
        return "redirect:/Category";
    }

    @GetMapping("/editar/{id}")
    public String editarCategoria(@PathVariable Long id, Model model) {
        model.addAttribute("categoria", categoryService.obtenerCategoriaPorId(id));
        return "dashboard/categorias-form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarCategoria(@PathVariable Long id) {
        categoryService.eliminarCategoria(id);
        return "redirect:/Category";
    }


}
