package in.tablese.tablese_core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // Enables WebSocket message handling, backed by a message broker.
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 1. Configure the message broker.
        // This is the destination for messages that will be broadcast to all connected clients.
        // Our server will send messages to destinations like "/topic/orders/1".
        config.enableSimpleBroker("/topic");

        // 2. Configure the prefix for application-level destinations.
        // This is the prefix for messages sent FROM clients TO the server (we won't use this yet).
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 3. Register the WebSocket endpoint.
        // This is the URL that our kitchen hub client will connect to.
        // "/ws" is a common convention for WebSocket endpoints.
        registry.addEndpoint("/ws").withSockJS();
    }
}