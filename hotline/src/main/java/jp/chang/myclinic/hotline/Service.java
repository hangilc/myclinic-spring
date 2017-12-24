package jp.chang.myclinic.hotline;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.dto.HotlineDTO;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
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
        @GET("list-todays-hotline")
        CompletableFuture<List<HotlineDTO>> listTodaysHotline();

        @GET("list-todays-hotline")
        Call<List<HotlineDTO>> listTodaysHotlineSync();

        @GET("list-recent-hotline")
        CompletableFuture<List<HotlineDTO>> listRecentHotline(@Query("threshold-hotline-id") int thresholdHotlineId);

        @GET("list-recent-hotline")
        Call<List<HotlineDTO>> listRecentHotlineSync(@Query("threshold-hotline-id") int thresholdHotlineId);

        @POST("enter-hotline")
        CompletableFuture<Integer> enterHotline(@Body HotlineDTO hotline);
    }

    public static ServerAPI api;

    public static void setServerUrl(String serverUrl){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        //logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        ObjectMapper mapper = new ObjectMapper();
        Retrofit server = new Retrofit.Builder()
                .baseUrl(serverUrl)
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .addCallAdapterFactory(Java8CallAdapterFactory.create())
                .client(httpClient.build())
                .build();
        api = server.create(ServerAPI.class);
    }

}
