package jp.chang.myclinic.hotline;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.dto.HotlineDTO;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.java8.Java8CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.GET;

import java.util.List;
import java.util.concurrent.CompletableFuture;

class Service {
    public interface ServerAPI {
        @GET("list-todays-hotline")
        CompletableFuture<List<HotlineDTO>> listTodaysHotline();

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
