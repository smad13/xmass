package com.xmas.greet.controlador.dto;



public class UsuarioRegistroDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private String direccion;
    private String correo;
    private String tipo_documento;
    private long numero_documento;

    private String password;
    private String rol;

public String getRol() {
    return rol;
}

public void setRol(String rol) {
    this.rol = rol;
}


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTipo_documento() {
        return tipo_documento;
    }

    public void setTipo_documento(String tipo_documento) {
        this.tipo_documento = tipo_documento;
    }

    public long getNumero_documento() {
        return numero_documento;
    }

    public void setNumero_documento(long numero_documento) {
        this.numero_documento = numero_documento;
    }



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UsuarioRegistroDTO() {
    }

    public UsuarioRegistroDTO(String nombre, String apellido, String direccion, String correo, String tipo_documento,
            long numero_documento, String password) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
        this.correo = correo;
        this.tipo_documento = tipo_documento;
        this.numero_documento = numero_documento;
        this.password = password;
    }

    
}