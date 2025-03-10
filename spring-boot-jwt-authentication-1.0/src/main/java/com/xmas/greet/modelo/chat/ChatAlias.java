package com.xmas.greet.modelo.chat;


import com.xmas.greet.modelo.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "chat_alias", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"chat_id", "usuario_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatAlias {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    @Getter @Setter
    private Chat chat;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @Getter @Setter
    private Usuario usuario;
    
    @Column(name = "alias")
    @Getter @Setter
    private String alias;
}
