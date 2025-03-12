package com.xmas.greet.seguridad;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Habilitamos un broker simple para enviar mensajes a los suscriptores.
        config.enableSimpleBroker("/topic");
        // Los mensajes enviados desde el cliente deben tener el prefijo /app.
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Se habilita el punto final para la comunicación WebSocket.
        registry.addEndpoint("/ws").withSockJS();
    }

}
