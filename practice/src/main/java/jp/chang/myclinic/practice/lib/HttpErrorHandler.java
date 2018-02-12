package jp.chang.myclinic.practice.lib;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import retrofit2.HttpException;

import java.io.IOException;

public class HttpErrorHandler {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MessageContainer {
        public String message;
    }

    public String getMessage(Throwable throwable){
        if( throwable instanceof HttpException){
            HttpException httpException = (HttpException)throwable;
            try {
                String body = httpException.response().errorBody().string();
                try {
                    MessageContainer messageContainer = objectMapper.readValue(body, MessageContainer.class);
                    return messageContainer.message;
                } catch(Exception ex){
                    return body;
                }
            } catch(IOException ioException){
                return throwable.getMessage();
            }
        } else {
            return throwable.getMessage();
        }
    }

}
