package com.xmas.greet.modelo.venta;


import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Getter @Setter
    private String id;
    @NotBlank @NotNull
    @Getter @Setter
    private String name;
    @NotNull @DecimalMin(value = "0.1")
    @Getter @Setter
    private Double price;
    @NotBlank @NotNull
    @Getter @Setter
    private String description;
    @NotNull
    @Getter @Setter
     @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category categoria;
    @NotNull
    @Getter @Setter
    private int stock;  

    @Lob
    @Getter @Setter
    @Column(name = "imagen", columnDefinition = "BYTEA")
    private byte[] imagen;
    


    @Getter @Setter
    @Transient
    private String base64Image;
}
