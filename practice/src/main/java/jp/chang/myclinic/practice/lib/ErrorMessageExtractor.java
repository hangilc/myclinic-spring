package jp.chang.myclinic.practice.lib;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import retrofit2.HttpException;

import java.io.IOException;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

public class ErrorMessageExtractor {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MessageContainer {
        public String message;
    }

    public static String extract(Throwable throwable){
        while( isWrapper(throwable) ){
            throwable = throwable.getCause();
        }
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
                return throwable.toString();
            }
        } else {
            return throwable.toString();
        }
    }

    private static boolean isWrapper(Throwable th){
        return (th instanceof CompletionException) || (th instanceof ExecutionException);
    }

}
