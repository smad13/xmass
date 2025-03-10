package com.xmas.greet.modelo;

import java.math.BigDecimal;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class DETALLE {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_detalle;
    private Integer id_pedido;
    private String id_producto;
    private Integer cantidad;
    private Double precio_unitario;
    private BigDecimal subtotal;
    private LocalDateTime fecha_registro;

   


    public String getId_producto() {
        return id_producto;
    }

    public void setId_producto(String id_producto) {
        this.id_producto = id_producto;
    }

    // Constructor vacío
    public DETALLE() {
    }

    // Constructor con parámetros
    public DETALLE(Long id_detalle, Integer id_pedido, String id_producto, Integer cantidad, Double precio_unitario,
            BigDecimal subtotal, LocalDateTime fecha_registro) {
        this.id_detalle = id_detalle;
        this.id_pedido = id_pedido;
        this.id_producto = id_producto;
        this.cantidad = cantidad;
        this.precio_unitario = precio_unitario;
        this.subtotal = subtotal;
        this.fecha_registro = fecha_registro;
    }

    public DETALLE(Integer id_pedido, String id_producto, Integer cantidad, Double precio_unitario, BigDecimal subtotal,
            LocalDateTime fecha_registro) {
        this.id_pedido = id_pedido;
        this.id_producto = id_producto;
        this.cantidad = cantidad;
        this.precio_unitario = precio_unitario;
        this.subtotal = subtotal;
        this.fecha_registro = fecha_registro;
    }

    public Long getId_detalle() {
        return id_detalle;
    }

    public void setId_detalle(Long id_detalle) {
        this.id_detalle = id_detalle;
    }

    public Integer getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(Integer id_pedido) {
        this.id_pedido = id_pedido;
    }


    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecio_unitario() {
        return precio_unitario;
    }

    public void setPrecio_unitario(Double precio_unitario) {
        this.precio_unitario = precio_unitario;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public LocalDateTime getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(LocalDateTime fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    // Getters y setters
    
}