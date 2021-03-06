package jp.chang.myclinic.serverpostgresql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    @Qualifier("practice-logger")
    private PublishingWebSocketHandler practiceLogHandler;

    @Autowired
    @Qualifier("hotline-logger")
    private PublishingWebSocketHandler hotlineLogHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(practiceLogHandler, "/practice-log");
        registry.addHandler(hotlineLogHandler, "/hotline");
    }

}
