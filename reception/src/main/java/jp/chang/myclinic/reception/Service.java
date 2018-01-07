package jp.chang.myclinic.reception;

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
        @GET("list-wqueue-full")
        CompletableFuture<List<WqueueFullDTO>> listWqueue();

        @GET("get-patient")
        CompletableFuture<PatientDTO> getPatient(@Query("patient-id") int patientId);

        @POST("enter-patient-with-hoken")
        CompletableFuture<PatientHokenListDTO> enterPatientWithHoken(@Body PatientHokenListDTO patientHokenListDTO);

        @POST("enter-patient")
        CompletableFuture<Integer> enterPatient(@Body PatientDTO patientDTO);

        @GET("list-hoken")
        CompletableFuture<HokenListDTO> listHoken(@Query("patient-id") int patientId);

        @GET("search-patient-by-name")
        CompletableFuture<List<PatientDTO>> searchPatientByName(@Query("last-name") String lastName,
                                                                @Query("first-name") String firstName);

        @GET("search-patient-by-yomi")
        CompletableFuture<List<PatientDTO>> searchPatientByYomi(@Query("last-name-yomi") String lastNameYomi,
                                                                @Query("first-name-yomi") String firstNameYomi);

        @GET("search-patient")
        CompletableFuture<List<PatientDTO>> searchPatient(@Query("text") String text);

        @GET("list-recently-registered-patients")
        CompletableFuture<List<PatientDTO>> listRecentlyRegisteredPatients();

        @POST("enter-shahokokuho")
        CompletableFuture<Integer> enterShahokokuho(@Body ShahokokuhoDTO shahokokuhoDTO);

        @POST("delete-shahokokuho")
        CompletableFuture<Boolean> deleteShahokokuho(@Body ShahokokuhoDTO shahokokuhoDTO);

        @POST("enter-koukikourei")
        CompletableFuture<Integer> enterKoukikourei(@Body KoukikoureiDTO koukikoureiDTO);

        @POST("delete-koukikourei")
        CompletableFuture<Boolean> deleteKoukikourei(@Body KoukikoureiDTO koukikoureiDTO);

        @POST("delete-roujin")
        CompletableFuture<Boolean> deleteRoujin(@Body RoujinDTO roujinDTO);

        @POST("enter-kouhi")
        CompletableFuture<Integer> enterKouhi(@Body KouhiDTO kouhiDTO);

        @POST("delete-kouhi")
        CompletableFuture<Boolean> deleteKouhi(@Body KouhiDTO kouhiDTO);

        @GET("get-visit-meisai")
        CompletableFuture<MeisaiDTO> getVisitMeisai(@Query("visit-id") int visitId);

        @GET("get-charge")
        CompletableFuture<ChargeDTO> getCharge(@Query("visit-id") int visitId);

        @GET("find-charge")
        CompletableFuture<ChargeOptionalDTO> findCharge(@Query("visit-id") int visitId);

        @GET("list-payment")
        CompletableFuture<List<PaymentDTO>> listPayment(@Query("visit-id") int visitId);

        @GET("list-recent-payment")
        CompletableFuture<List<PaymentVisitPatientDTO>> listRecentPayment(@Query("n") int n);

        @GET("list-payment-by-patient")
        CompletableFuture<List<PaymentVisitPatientDTO>> listPaymentByPatient(@Query("patient-id") int patientId,
                                                                             @Query("n") int n);

        @GET("get-clinic-info")
        CompletableFuture<ClinicInfoDTO> getClinicInfo();

        @POST("start-visit")
        CompletableFuture<Integer> startVisit(@Query("patient-id") int patientId);

        @GET("get-visit")
        CompletableFuture<VisitDTO> getVisit(@Query("visit-id") int visitId);

        @POST("finish-cashier")
        CompletableFuture<Boolean> finishCashier(@Body PaymentDTO payment);

        @POST("delete-visit-from-reception")
        CompletableFuture<Boolean> deleteVisitFromReception(@Query("visit-id") int visitId);
    }

    public static ServerAPI api;
    public static OkHttpClient client;
    public static ObjectMapper mapper;

    public static void setServerUrl(String serverUrl) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        client = httpClient.build();

        mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Op.class, new JacksonOpDeserializer());
        mapper.registerModule(module);
        Retrofit server = new Retrofit.Builder()
                .baseUrl(serverUrl)
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .addCallAdapterFactory(Java8CallAdapterFactory.create())
                .client(client)
                .build();
        api = server.create(ServerAPI.class);
    }

}