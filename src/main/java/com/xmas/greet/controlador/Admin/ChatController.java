package com.xmas.greet.controlador.Admin;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xmas.greet.modelo.Usuario;
import com.xmas.greet.modelo.chat.Chat;
import com.xmas.greet.modelo.chat.ChatAlias;
import com.xmas.greet.modelo.chat.ChatMessage;
import com.xmas.greet.modelo.chat.Message;
import com.xmas.greet.servicio.ChatService;
import com.xmas.greet.servicio.UsuarioServicio;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Crear un nuevo chat
    @PostMapping("/crear")
    public Chat createChat(@RequestParam String nombre, @RequestParam String user2Correo, Principal principal) {
        Usuario user1 = usuarioServicio.obtenerPorCorreo(principal.getName());
        Usuario user2 = usuarioServicio.obtenerPorCorreo(user2Correo); // Buscar usuario por correo
        if (user2 == null) {
            throw new RuntimeException("Usuario con correo " + user2Correo + " no encontrado");
        }
        return chatService.createChat(nombre, user1, user2);
    }
    // Obtener los chats del usuario autenticado
    @GetMapping
    public List<Chat> getUserChats(Principal principal) {
        Usuario user = usuarioServicio.obtenerPorCorreo(principal.getName());
        return chatService.getChatsForUser(user);
    }


    @PutMapping("/{chatId}/alias")
    public ResponseEntity<ChatAlias> updateAlias(@PathVariable Long chatId, @RequestParam String alias, Principal principal) {
        Usuario user = usuarioServicio.obtenerPorCorreo(principal.getName());
        ChatAlias updatedAlias = chatService.updateChatAlias(chatId, user, alias);
        return ResponseEntity.ok(updatedAlias);
    }

    // Obtener un chat por ID
    @GetMapping("/{id}")
    public Optional<Chat> getChatById(@PathVariable Long id) {
        return chatService.getChatById(id);
    }

    // Eliminar un chat
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChat(@PathVariable Long id) {
        chatService.deleteChat(id);
        return ResponseEntity.noContent().build();
    }

    // Enviar un mensaje
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(ChatMessage chatMessage, Principal principal) {
        Usuario sender = usuarioServicio.obtenerPorCorreo(principal.getName());
        Message savedMessage = chatService.sendMessage(chatMessage.getChatId(), sender, chatMessage.getContent());
        messagingTemplate.convertAndSend("/topic/chat." + chatMessage.getChatId(), savedMessage);
    }


@GetMapping("/{id}/messages")
public List<Message> getChatMessages(@PathVariable Long id) {
    return chatService.getChatHistory(id);
}
    
}