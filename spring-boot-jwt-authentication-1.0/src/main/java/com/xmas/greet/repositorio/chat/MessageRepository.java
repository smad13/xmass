package com.xmas.greet.repositorio.chat;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xmas.greet.modelo.chat.Chat;
import com.xmas.greet.modelo.chat.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChat(Chat chat);
}