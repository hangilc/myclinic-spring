package jp.chang.myclinic.practice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import jp.chang.myclinic.drawer.JacksonOpDeserializer;
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitFullPageDTO;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.java8.Java8CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Service {
    public interface ServerAPI {
        @GET("search-patient")
        CompletableFuture<List<PatientDTO>> searchPatient(@Query("text") String text);

        @GET("list-visit-full")
        CompletableFuture<VisitFullPageDTO> listVisitFull(@Query("patient-id") int patientId, @Query("page") int page);
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