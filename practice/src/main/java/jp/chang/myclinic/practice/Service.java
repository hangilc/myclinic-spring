package jp.chang.myclinic.practice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import jp.chang.myclinic.drawer.JacksonOpDeserializer;
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.dto.*;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.java8.Java8CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Service {
    public interface ServerAPI {
        @GET("search-patient")
        CompletableFuture<List<PatientDTO>> searchPatient(@Query("text") String text);

        @GET("list-visit-full2")
        CompletableFuture<VisitFull2PageDTO> listVisitFull2(@Query("patient-id") int patientId, @Query("page") int page);

        @POST("update-text")
        CompletableFuture<Boolean> updateText(@Body TextDTO textDTO);

        @POST("enter-text")
        CompletableFuture<Integer> enterText(@Body TextDTO textDTO);

        @POST("delete-text")
        CompletableFuture<Boolean> deleteText(@Query("text-id") int textId);

        @GET("list-available-hoken")
        CompletableFuture<HokenDTO> listAvailableHoken(@Query("patient-id") int patientId, @Query("at") String at);

        @POST("update-hoken")
        CompletableFuture<Boolean> updateHoken(@Body VisitDTO visitDTO);
    }

    public static ServerAPI api;

    public static void setServerUrl(String serverUrl){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        //logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Op.class, new JacksonOpDeserializer());
        mapper.registerModule(module);
        Retrofit server = new Retrofit.Builder()
                .baseUrl(serverUrl)
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .addCallAdapterFactory(Java8CallAdapterFactory.create())
                .client(httpClient.build())
                .build();
        api = server.create(ServerAPI.class);
    }

}