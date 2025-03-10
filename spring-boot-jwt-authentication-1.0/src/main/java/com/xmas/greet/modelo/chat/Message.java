package com.xmas.greet.modelo.chat;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xmas.greet.modelo.Usuario;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id;
    
    @ManyToOne
    @JsonIgnoreProperties({"messages"})
    @Getter @Setter
    private Chat chat;

    
    @ManyToOne
    @JoinColumn(name = "sender_id")
    @Getter @Setter
    private Usuario sender;
    
    @Getter @Setter
    private String content;
    
    @Getter @Setter
    private LocalDateTime timestamp;
    
    @Getter @Setter
    private boolean leido;
}