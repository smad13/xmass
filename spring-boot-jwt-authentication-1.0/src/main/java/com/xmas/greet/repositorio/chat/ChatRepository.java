package com.xmas.greet.repositorio.chat;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xmas.greet.modelo.chat.Chat;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    List<Chat> findByUser1_IdOrUser2_Id(Long user1Id, Long user2Id);



}