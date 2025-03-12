package com.xmas.greet.repositorio;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

import com.xmas.greet.modelo.Usuario;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long>{

	public Usuario findByCorreo(String correo);
	
    @Query("SELECT u FROM Usuario u JOIN u.roles r")
    List<Usuario> findAllUsuariosWithRoles();
	

}
