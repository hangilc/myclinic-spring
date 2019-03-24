package jp.chang.myclinic.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

public class Client {

    private Service.ServerAPI api;
    private OkHttpClient client;
    private HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

    public Client(String serverUrl) {
        if (!serverUrl.endsWith("/")) {
            serverUrl += "/";
        }
        logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        this.client = httpClient.build();

        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        mapper.registerModule(module);
        Retrofit server = new Retrofit.Builder()
                .baseUrl(serverUrl)
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .client(client)
                .build();
        this.api = server.create(Service.ServerAPI.class);
    }

    public Service.ServerAPI getApi(){
        return api;
    }

    public void setLogBody(){
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    public void clearLogBody(){
        logging.setLevel(HttpLoggingInterceptor.Level.NONE);
    }

    public void stop(){
        client.dispatcher().executorService().shutdown();
        client.connectionPool().evictAll();
        Cache cache = client.cache();
        if (cache != null) {
            try {
                cache.close();
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }
    }

}
