package jp.chang.myclinic.practice.lib;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.HttpException;

import java.io.IOException;
import java.util.function.Consumer;

public class HttpExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(HttpExceptionHandler.class);
    private static ObjectMapper objectMapper = new ObjectMapper();

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MessageContainer {
        public String message;
    }

    private Consumer<String> messageHandler;

    public HttpExceptionHandler(Consumer<String> messageHandler){
        this.messageHandler = messageHandler;
    }

    public void handle(Throwable throwable){
        logger.error("error:", throwable);
        if( throwable instanceof HttpException){
            HttpException httpException = (HttpException)throwable;
            try {
                String body = httpException.response().errorBody().string();
                try {
                    MessageContainer messageContainer = objectMapper.readValue(body, MessageContainer.class);
                    messageHandler.accept(messageContainer.message);
                } catch(Exception ex){
                    System.out.println("JSON parse error: " + ex);
                    messageHandler.accept(body);
                }
            } catch(IOException ioException){
                messageHandler.accept(throwable.getMessage());
            }
        } else {
            messageHandler.accept(throwable.getMessage());
        }
    }

}
