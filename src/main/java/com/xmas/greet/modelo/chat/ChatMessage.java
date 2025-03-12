package com.xmas.greet.modelo.chat;

import lombok.Data;

@Data
public class ChatMessage {
    private Long chatId;
    private String content;
}
