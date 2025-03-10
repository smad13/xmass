package com.xmas.greet.servicio;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmas.greet.modelo.Usuario;
import com.xmas.greet.modelo.chat.Chat;
import com.xmas.greet.modelo.chat.ChatAlias;
import com.xmas.greet.modelo.chat.Message;
import com.xmas.greet.repositorio.chat.ChatAliasRepository;
import com.xmas.greet.repositorio.chat.ChatRepository;
import com.xmas.greet.repositorio.chat.MessageRepository;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ChatAliasRepository chatAliasRepository;

    public ChatService(ChatRepository chatRepository, MessageRepository messageRepository, ChatAliasRepository chatAliasRepository) {
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
        this.chatAliasRepository = chatAliasRepository;
    }

    
    // Crear un nuevo chat entre dos usuarios
    public Chat createChat(String nombre, Usuario user1, Usuario user2) {
        Chat chat = new Chat();
        // El campo global "nombre" se usa para identificar el chat
        chat.setNombre(nombre);
        chat.setUser1(user1);
        chat.setUser2(user2);
        Chat savedChat = chatRepository.save(chat);
        
        // Guardar el alias para el usuario creador
        ChatAlias aliasCreator = new ChatAlias();
        aliasCreator.setChat(savedChat);
        aliasCreator.setUsuario(user1);
        aliasCreator.setAlias(nombre);
        chatAliasRepository.save(aliasCreator);
        
        // Para el otro usuario se creará (o asignará) más adelante
        return savedChat;
    }

    
    public ChatAlias updateChatAlias(Long chatId, Usuario usuario, String alias) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat no encontrado"));
        Optional<ChatAlias> optionalAlias = chatAliasRepository.findByChatAndUsuario(chat, usuario);
        ChatAlias chatAlias;
        if(optionalAlias.isPresent()){
            chatAlias = optionalAlias.get();
            chatAlias.setAlias(alias);
        } else {
            chatAlias = new ChatAlias();
            chatAlias.setChat(chat);
            chatAlias.setUsuario(usuario);
            chatAlias.setAlias(alias);
        }
        return chatAliasRepository.save(chatAlias);
    }

    // Enviar un mensaje
    public Message sendMessage(Long chatId, Usuario sender, String content) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat no encontrado"));
        Message message = new Message();
        message.setChat(chat);
        message.setSender(sender);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        message.setLeido(false); // O message.setRead(false) según el nombre que uses
        return messageRepository.save(message);
    }

        
        public List<Message> getChatHistory(Long chatId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat no encontrado"));
        return messageRepository.findByChat(chat);
    }
    

    // Obtener todos los chats de un usuario
    public List<Chat> getChatsForUser(Usuario user) {
        return chatRepository.findByUser1_IdOrUser2_Id(user.getId(), user.getId());
    }
    

    // Obtener un chat por ID
    public Optional<Chat> getChatById(Long id) {
        return chatRepository.findById(id);
    }


    // Eliminar un chat
    public void deleteChat(Long id) {
        chatRepository.deleteById(id);
    }

    public List<Chat> getAllChats() {
        return chatRepository.findAll();
    }
}