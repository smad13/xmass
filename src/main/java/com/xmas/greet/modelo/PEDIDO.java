package com.xmas.greet.modelo;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class PEDIDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_pedido;

    private String direccion_envio;
    private Double valor_total;

    @Column(name = "id_usuario")
    private Integer id_cliente;
    
    private String estado;

    // Constructor completo
    public PEDIDO(Long id_pedido , String direccion_envio, Double valor_total,
                  Integer id_cliente, String estado) {
        this.id_pedido = id_pedido;
        this.direccion_envio = direccion_envio;
        this.valor_total = valor_total;
        this.id_cliente = id_cliente;
        this.estado = estado;
    }

    // Constructor sin id_pedido
    public PEDIDO( String direccion_envio, Double valor_total,
                  Integer id_cliente, String estado) {
        this.direccion_envio = direccion_envio;
        this.valor_total = valor_total;
        this.id_cliente = id_cliente;
        this.estado = estado;
    }

    // Constructor vacío
    public PEDIDO() {
    }

    // Getters y setters
    public Long getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(Long id_pedido) {
        this.id_pedido = id_pedido;
    }


    public String getDireccion_envio() {
        return direccion_envio;
    }

    public void setDireccion_envio(String direccion_envio) {
        this.direccion_envio = direccion_envio;
    }

    public Double getValor_total() {
        return valor_total;
    }

    public void setValor_total(Double valor_total) {
        this.valor_total = valor_total;
    }

    public Integer getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(Integer id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
