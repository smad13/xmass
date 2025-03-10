package com.xmas.greet.servicio.venta;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmas.greet.modelo.venta.Category;
import com.xmas.greet.repositorio.venta.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> listarCategorias() {
        return categoryRepository.findAll();
    }

    public Category guardarCategoria(Category category) {
        return categoryRepository.save(category);
    }

    public Category obtenerCategoriaPorId(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public void eliminarCategoria(Long id) {
        categoryRepository.deleteById(id);
    }
}
