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
import java.util.Map;
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
        CompletableFuture<Integer> enterDrug(@Body DrugDTO drug);

        @GET("get-drug-full")
        CompletableFuture<DrugFullDTO> getDrugFull(@Query("drug-id") int drugId);

        @POST("start-visit")
        CompletableFuture<Integer> startVisit(@Query("patient-id") int patientId);

        @GET("list-wqueue-full")
        CompletableFuture<List<WqueueFullDTO>> listWqueueFull();

        @POST("start-exam")
        CompletableFuture<Boolean> startExam(@Query("visit-id") int visitId);

        @POST("suspend-exam")
        CompletableFuture<Boolean> suspendExam(@Query("visit-id") int visitId);

        @POST("end-exam")
        CompletableFuture<Boolean> endExam(@Query("visit-id") int visitId, @Query("charge") int charge);

        @GET("get-text")
        CompletableFuture<TextDTO> getText(@Query("text-id") int textId);

        @GET("list-drug-full")
        CompletableFuture<List<DrugFullDTO>> listDrugFull(@Query("visit-id") int visitId);

        @GET("list-drug-full-by-drug-ids")
        CompletableFuture<List<DrugFullDTO>> listDrugFullByDrugIds(@Query("drug-id") List<Integer> drugIds);

        @GET("batch-resolve-iyakuhin-master")
        CompletableFuture<Map<Integer, IyakuhinMasterDTO>> batchResolveIyakuhinMaster(@Query("iyakuhincode") List<Integer> iyakuhincodes,
                                                                                        @Query("at") String at);

        @POST("batch-enter-drugs")
        CompletableFuture<List<Integer>> batchEnterDrugs(@Body List<DrugDTO> drugs);

        @POST("batch-update-drug-days")
        CompletableFuture<Boolean> batchUpdateDrugDays(@Query("drug-id") List<Integer> drugIds, @Query("days") int days);

        @POST("batch-delete-drugs")
        CompletableFuture<Boolean> batchDeleteDrugs(@Query("drug-id") List<Integer> drugIds);

        @POST("delete-drug")
        CompletableFuture<Boolean> deleteDrug(@Query("drug-id") int drugId);

        @POST("update-drug")
        CompletableFuture<Boolean> updateDrug(@Body DrugDTO drug);

        @POST("batch-enter-shinryou-by-name")
        CompletableFuture<BatchEnterResultDTO> batchEnterShinryouByName(@Query("name") List<String> names,
                                                                  @Query("visit-id") int visitId);

        @POST("enter-shinryou")
        CompletableFuture<Integer> enterShinryou(@Body ShinryouDTO shinryou);

        @POST("update-shinryou")
        CompletableFuture<Boolean> updateShinryou(@Body ShinryouDTO shinryou);

        @POST("delete-shinryou")
        CompletableFuture<Boolean> deleteShinryou(@Query("shinryou-id") int shinryouId);

        @GET("list-shinryou-full-by-ids")
        CompletableFuture<List<ShinryouFullDTO>> listShinryouFullByIds(@Query("shinryou-id") List<Integer> shinryouIds);

        @GET("list-conduct-full-by-ids")
        CompletableFuture<List<ConductFullDTO>> listConductFullByIds(@Query("conduct-id") List<Integer> conductIds);

        @GET("search-shinryou-master")
        CompletableFuture<List<ShinryouMasterDTO>> searchShinryouMaster(@Query("text") String text, @Query("at") String at);

        @GET("get-shinryou-full")
        CompletableFuture<ShinryouFullDTO> getShinryouFull(@Query("shinryou-id") int shinryouId);

        @POST("batch-delete-shinryou")
        CompletableFuture<Boolean> batchDeleteShinryou(@Query("shinryou-id") List<Integer> shinryouIds);

        @POST("batch-copy-shinryou")
        CompletableFuture<List<Integer>> batchCopyShinryou(@Query("visit-id") int visitId,
                                                           @Body List<ShinryouDTO> srcList);

        @POST("delete-duplicate-shinryou")
        CompletableFuture<List<Integer>> deleteDuplicateShinryou(@Query("visit-id") int visitId);

        @GET("get-conduct-full")
        CompletableFuture<ConductFullDTO> getConductFull(@Query("conduct-id") int conductId);

        @POST("enter-xp")
        CompletableFuture<Integer> enterXp(@Query("visit-id") int visitId, @Query("label") String label, @Query("film") String film);
    }

    public static ServerAPI api;

    static void setServerUrl(String serverUrl){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        //logging.setLevel(HttpLoggingInterceptor.Level.BODY);
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