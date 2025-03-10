package com.xmas.greet.servicio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.xmas.greet.controlador.dto.UsuarioRegistroDTO;
import com.xmas.greet.modelo.Rol;
import com.xmas.greet.modelo.Usuario;
import com.xmas.greet.repositorio.PasswordRepository;
import com.xmas.greet.repositorio.RolRepositorio;
import com.xmas.greet.repositorio.UsuarioRepositorio;

@Service
public class UsuarioServicioImpl implements UsuarioServicio {

	
	private UsuarioRepositorio usuarioRepositorio;
	private RolRepositorio rolRepositorio;
	private final PasswordRepository passwordRepository;

	@Autowired
	@Lazy
	private BCryptPasswordEncoder passwordEncoder;
	
	public UsuarioServicioImpl(UsuarioRepositorio usuarioRepositorio,RolRepositorio rolRepositorio, PasswordRepository passwordRepository) {
		super();
		this.usuarioRepositorio = usuarioRepositorio;
		this.rolRepositorio = rolRepositorio;
		this.passwordRepository = passwordRepository;
	}

	@Override
    public Usuario guardar(UsuarioRegistroDTO registroDTO) {
        // Obtención del rol por defecto o el rol especificado
        Rol rolCliente = rolRepositorio.findByNombre("ROLE_CLIENTE");
        Rol rolElegido = rolCliente;

        if (registroDTO.getRol() != null && !registroDTO.getRol().isEmpty()) {
            rolElegido = rolRepositorio.findByNombre(registroDTO.getRol());
        }

        // Creación del nuevo usuario
        Usuario usuario = new Usuario(
                registroDTO.getNombre(),
                registroDTO.getApellido(),
                registroDTO.getDireccion(),
                registroDTO.getCorreo(),
                registroDTO.getTipo_documento(),
                registroDTO.getNumero_documento(),
                passwordEncoder.encode(registroDTO.getPassword()), // Codificación de la contraseña
                Arrays.asList(rolElegido)
        );
        return usuarioRepositorio.save(usuario);
    }

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepositorio.findByCorreo(username);
		if(usuario == null) {
			throw new UsernameNotFoundException("Usuario o password inválidos");
		}
		return new User(usuario.getCorreo(),usuario.getPassword(), mapearAutoridadesRoles(usuario.getRoles()));
	}

	private Collection<? extends GrantedAuthority> mapearAutoridadesRoles(Collection<Rol> roles){
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getNombre())).collect(Collectors.toList());
	}
	
	@Override
	public List<Usuario> listarUsuarios() {
		return usuarioRepositorio.findAll();
	}

	@Override
    public Usuario findByCorreo(String correo) {
        return passwordRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo: " + correo));
    }
	@Override
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepositorio.findAllUsuariosWithRoles();
    }

	@Override
	public Usuario obtenerPorCorreo(String correo) {
	return usuarioRepositorio.findByCorreo(correo);
	}

	@Override
    public Usuario editarUsuario(UsuarioRegistroDTO registroDTO) {
        Usuario usuarioExistente = usuarioRepositorio.findById(registroDTO.getId())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con id: " + registroDTO.getId()));
        usuarioExistente.setNombre(registroDTO.getNombre());
        usuarioExistente.setApellido(registroDTO.getApellido());
        usuarioExistente.setDireccion(registroDTO.getDireccion());
        usuarioExistente.setCorreo(registroDTO.getCorreo());
        usuarioExistente.setTipo_documento(registroDTO.getTipo_documento());
        usuarioExistente.setNumero_documento(registroDTO.getNumero_documento());
        
        // Actualiza la contraseña solo si se envía un nuevo valor
        if (registroDTO.getPassword() != null && !registroDTO.getPassword().isEmpty()) {
            usuarioExistente.setPassword(passwordEncoder.encode(registroDTO.getPassword()));
        }
        
        // Actualiza el rol si se envía uno nuevo
        if (registroDTO.getRol() != null && !registroDTO.getRol().isEmpty()) {
            Rol rol = rolRepositorio.findByNombre(registroDTO.getRol());
			usuarioExistente.setRoles(new ArrayList<>(Arrays.asList(rol)));

        }
        return usuarioRepositorio.save(usuarioExistente);
    }
    
    @Override
    public Usuario obtenerPorId(Long id) {
        return usuarioRepositorio.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con id: " + id));
    }

    @Override
    public List<Usuario> getUsuariosByRole(String roles) {
        String roleName = "ROLE_" + roles.toUpperCase();
        return usuarioRepositorio.findAll().stream()
            .filter(u -> u.getRoles().stream().anyMatch(r -> r.getNombre().equals(roleName)))
            .collect(Collectors.toList());
    }    
}