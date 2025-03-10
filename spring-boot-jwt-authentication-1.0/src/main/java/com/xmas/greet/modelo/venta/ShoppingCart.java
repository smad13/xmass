package com.xmas.greet.modelo.venta;

import com.xmas.greet.modelo.Usuario;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class ShoppingCart {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Usar IDENTITY para Long
    private Long id;

    @ManyToOne(optional = false, cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private Product product;

    @ManyToOne(optional = false, cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private Usuario usuario;

    @NotNull
    private int amount;
}
