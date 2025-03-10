package com.xmas.greet.repositorio.chat;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xmas.greet.modelo.Usuario;
import com.xmas.greet.modelo.chat.Chat;
import com.xmas.greet.modelo.chat.ChatAlias;

@Repository
public interface ChatAliasRepository extends JpaRepository<ChatAlias, Long> {
    Optional<ChatAlias> findByChatAndUsuario(Chat chat, Usuario usuario);
}