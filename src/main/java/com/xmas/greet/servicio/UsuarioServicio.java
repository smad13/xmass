package com.xmas.greet.servicio;

import java.util.List;


import org.springframework.security.core.userdetails.UserDetailsService;

import com.xmas.greet.controlador.dto.UsuarioRegistroDTO;
import com.xmas.greet.modelo.Usuario;



public interface UsuarioServicio extends UserDetailsService{

	public Usuario guardar(UsuarioRegistroDTO registroDTO);
	
	public List<Usuario> listarUsuarios();

	public List<Usuario> obtenerTodosLosUsuarios();
	
	Usuario findByCorreo(String correo);

    public Usuario obtenerPorCorreo(String correo);

	public Usuario editarUsuario( UsuarioRegistroDTO usuarioRegistroDTO);
    
    public Usuario obtenerPorId(Long id);

	List<Usuario> getUsuariosByRole(String roles);
	
}
