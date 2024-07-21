package org.andrelucs.SpringChatServer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/room","/user/");
        //registry.enableSimpleBroker("/queue");
        registry.setApplicationDestinationPrefixes("/chat");
        registry.setUserDestinationPrefix("/user");

        WebSocketMessageBrokerConfigurer.super.configureMessageBroker(registry);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chatapp");

        WebSocketMessageBrokerConfigurer.super.registerStompEndpoints(registry);
    }


}
