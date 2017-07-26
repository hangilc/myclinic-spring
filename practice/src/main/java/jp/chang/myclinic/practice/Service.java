package jp.chang.myclinic.practice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import jp.chang.myclinic.drawer.JacksonOpDeserializer;
import jp.chang.myclinic.drawer.Op;
import retrofit2.Retrofit;
import retrofit2.adapter.java8.Java8CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.concurrent.CompletableFuture;

public class Service {
    public interface ServerAPI {
        @POST("presc-done")
        CompletableFuture<Boolean> prescDone(@Query("visit-id") int visitId);
    }

    public static ServerAPI api;

    public static void setServerUrl(String serverUrl){
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Op.class, new JacksonOpDeserializer());
        mapper.registerModule(module);
        Retrofit server = new Retrofit.Builder()
                .baseUrl(serverUrl)
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .addCallAdapterFactory(Java8CallAdapterFactory.create())
                .build();
        api = server.create(ServerAPI.class);
    }

}