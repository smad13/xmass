package com.xmas.greet.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name ="material")
public class MATERIAL {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="ID")
    private Long ID_materiales;
    @Column(name ="Nombre")
    private String Nombre_materiales;
    @Column(name ="Descripcion")
    private String Descripcion_materiales;
    @Column(name ="Cantidad")
    private Integer Cantidad_materiales;
    @Column(name ="Precio")
    private Double Precio_materiales; 
    @Column(name ="Categoria")
    private String Categoria_materiales;

    public MATERIAL(Long ID_materiales, String Nombre_materiales, String Descripcion_materiales,
            Integer Cantidad_materiales, Double Precio_materiales, String Categoria_materiales) {
        this.ID_materiales = ID_materiales;
        this.Nombre_materiales = Nombre_materiales;
        this.Descripcion_materiales = Descripcion_materiales;
        this.Cantidad_materiales = Cantidad_materiales;
        this.Precio_materiales = Precio_materiales;
        this.Categoria_materiales = Categoria_materiales;
    }

    public MATERIAL(String Nombre_materiales, String Descripcion_materiales, Integer Cantidad_materiales,
            Double Precio_materiales, String Categoria_materiales) {

        this.Nombre_materiales = Nombre_materiales;
        this.Descripcion_materiales = Descripcion_materiales;
        this.Cantidad_materiales = Cantidad_materiales;
        this.Precio_materiales = Precio_materiales;
        this.Categoria_materiales = Categoria_materiales;
    }

    public MATERIAL() {
    }

    public Long getID_materiales() {
        return ID_materiales;
    }

    public void setID_materiales(Long iD_materiales) {
        ID_materiales = iD_materiales;
    }

    public String getNombre_materiales() {
        return Nombre_materiales;
    }

    public void setNombre_materiales(String nombre_materiales) {
        Nombre_materiales = nombre_materiales;
    }

    public String getDescripcion_materiales() {
        return Descripcion_materiales;
    }

    public void setDescripcion_materiales(String descripcion_materiales) {
        Descripcion_materiales = descripcion_materiales;
    }

    public Integer getCantidad_materiales() {
        return Cantidad_materiales;
    }

    public void setCantidad_materiales(Integer cantidad_materiales) {
        Cantidad_materiales = cantidad_materiales;
    }

    public Double getPrecio_materiales() {
        return Precio_materiales;
    }

    public void setPrecio_materiales(Double precio_materiales) {
        Precio_materiales = precio_materiales;
    }

    public String getCategoria_materiales() {
        return Categoria_materiales;
    }

    public void setCategoria_materiales(String categoria_materiales) {
        Categoria_materiales = categoria_materiales;
    }

}