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

        @GET("get-visit")
        CompletableFuture<VisitDTO> getVisit(@Query("visit-id") int visitId);

        @GET("get-hoken")
        CompletableFuture<HokenDTO> getHoken(@Query("visit-id") int visitId);

        @GET("search-iyakuhin-master-by-name")
        CompletableFuture<List<IyakuhinMasterDTO>> searchIyakuhinMaster(@Query("text") String text, @Query("at") String at);

        @GET("search-presc-example-full-by-name")
        CompletableFuture<List<PrescExampleFullDTO>> searchPrescExample(@Query("text") String text);

        @GET("search-prev-drug")
        CompletableFuture<List<DrugFullDTO>> searchPrevDrug(@Query("text") String text, @Query("patient-id") int patientId);

        @GET("resolve-iyakuhin-master")
        CompletableFuture<IyakuhinMasterDTO> resolveIyakuhinMaster(@Query("iyakuhincode") int iyakuhincode,
                                                                             @Query("at") String at);

        @POST("enter-drug")
        CompletableFuture<DrugDTO> enterDrug(@Body DrugDTO drug);

        @GET("get-drug-full")
        CompletableFuture<DrugFullDTO> getDrugFull(@Query("drug-id") int drugId);

        @POST("start-visit")
        CompletableFuture<Integer> startVisit(@Query("patient-id") int patientId);
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