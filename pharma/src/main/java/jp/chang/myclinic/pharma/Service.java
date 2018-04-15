package jp.chang.myclinic.pharma;

/**
 * Created by hangil on 2017/06/11.
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import jp.chang.myclinic.drawer.JacksonOpDeserializer;
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.dto.*;
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
        @GET("list-pharma-queue-full-for-prescription")
        CompletableFuture<List<PharmaQueueFullDTO>> listPharmaQueueForPrescription();

        @GET("list-pharma-queue-full-for-today")
        CompletableFuture<List<PharmaQueueFullDTO>> listPharmaQueueForToday();

        @GET("list-drug-full")
        CompletableFuture<List<DrugFullDTO>> listDrugFull(@Query("visit-id") int visitId);

        @GET("get-pharma-drug")
        CompletableFuture<PharmaDrugDTO> getPharmaDrug(@Query("iyakuhincode") int iyakuhincode);

        @GET("find-pharma-drug")
        CompletableFuture<PharmaDrugDTO> findPharmaDrug(@Query("iyakuhincode") int iyakuhincode);

        @POST("enter-pharma-drug")
        CompletableFuture<Boolean> enterPharmaDrug(@Body PharmaDrugDTO pharmaDrugDTO);

        @POST("update-pharma-drug")
        CompletableFuture<Boolean> updatePharmaDrug(@Body PharmaDrugDTO pharmaDrugDTO);

        @GET("get-clinic-info")
        CompletableFuture<ClinicInfoDTO> getClinicInfo();

        @GET("get-clinic-info")
        Call<ClinicInfoDTO> getClinicInfoCall();

        @GET("collect-pharma-drug-by-iyakuhincodes")
        CompletableFuture<List<PharmaDrugDTO>> collectPharmaDrugByIyakuhincodes(@Query("iyakuhincode[]") List<Integer> iyakuhincodes);

        @GET("list-visit-id-visited-at-for-patient")
        CompletableFuture<List<VisitIdVisitedAtDTO>> listVisitIdVisitedAtForPatient(@Query("patient-id") int patientId);

        @GET("list-visit-text-drug")
        CompletableFuture<List<VisitTextDrugDTO>> listVisitTextDrug(@Query("visit-id[]") List<Integer> visitIds);

        @GET("list-visit-text-drug-for-patient")
        CompletableFuture<VisitTextDrugPageDTO> listVisitTextDrugForPatient(@Query("patient-id") int patientId,
                                                                            @Query("page") int page);

        @GET("list-visit-text-drug-by-patient-and-iyakuhincode")
        CompletableFuture<VisitTextDrugPageDTO> listVisitTextDrugByPatientAndIyakuhincode(
                @Query("patient-id") int patientId, @Query("iyakuhincode") int iyakuhincode,
                @Query("page") int page
        );

        @GET("list-iyakuhin-for-patient")
        CompletableFuture<List<IyakuhincodeNameDTO>> listIyakuhinForPatient(@Query("patient-id") int patientId);

        @GET("list-visit-id-visited-at-by-patient-and-iyakuhincode")
        CompletableFuture<List<VisitIdVisitedAtDTO>> listVisitIdVisitedAtByPatientAndIyakuhincode(@Query("patient-id") int patientId,
                                                                                            @Query("iyakuhincode") int iyakuhincode);

        @GET("get-patient")
        CompletableFuture<PatientDTO> getPatient(@Query("patient-id") int patientId);

        @GET("find-patient")
        CompletableFuture<PatientDTO> findPatient(@Query("patient-id") int patientId);

        @GET("search-iyakuhin-master-by-name")
        CompletableFuture<List<IyakuhinMasterDTO>> searchIyakuhinMasterByName(@Query("text") String text, @Query("at") String at);

        @GET("search-pharma-drug-names")
        CompletableFuture<List<PharmaDrugNameDTO>> searchPharmaDrugNames(@Query("text") String text);

        @GET("list-all-pharma-drug-names")
        CompletableFuture<List<PharmaDrugNameDTO>> listAllPharmaDrugNames();

        @POST("delete-pharma-drug")
        CompletableFuture<Boolean> deletePharmaDrug(@Query("iyakuhincode") int iyakuhincode);

        @POST("presc-done")
        CompletableFuture<Boolean> prescDone(@Query("visit-id") int visitId);

        @GET("get-visit")
        CompletableFuture<VisitDTO> getVisit(@Query("visit-id") int visitId);

        @GET("page-visit-drug")
        CompletableFuture<VisitDrugPageDTO> pageVisitDrug(@Query("patient-id") int patientId,
                                                          @Query("page") int page);
    }

    public static ServerAPI api;
    static OkHttpClient client;

    static void setServerUrl(String serverUrl){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        //logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        client = httpClient.build();

        ObjectMapper mapper = new ObjectMapper();
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