package com.xmas.greet.modelo.chat;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xmas.greet.modelo.Usuario;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;




@Entity
@Table(name = "chats")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id;

    @Getter @Setter
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "user1_id")
    @Getter @Setter
    private Usuario user1;

    @ManyToOne
    @JoinColumn(name = "user2_id")
    @Getter @Setter
    private Usuario user2;

    @OneToMany(mappedBy = "chat", fetch = FetchType.LAZY)
    @JsonIgnore
    @Getter @Setter 
    private List<Message> messages;


}
