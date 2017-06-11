package jp.chang.myclinic.pharma;

/**
 * Created by hangil on 2017/06/11.
 */
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import jp.chang.myclinic.drawer.JacksonOpDeserializer;
import jp.chang.myclinic.drawer.Op;
import retrofit2.Retrofit;
import retrofit2.adapter.java8.Java8CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

class Service {
    public interface ServerAPI {
//        @GET("list-wqueue-full")
//        CompletableFuture<List<WqueueFullDTO>> listWqueue();
//
//        @GET("get-patient")
//        CompletableFuture<PatientDTO> getPatient(@Query("patient-id") int patientId);
//
//        @POST("enter-patient-with-hoken")
//        CompletableFuture<PatientHokenListDTO> enterPatientWithHoken(@Body PatientHokenListDTO patientHokenListDTO);
//
//        @POST("enter-patient")
//        CompletableFuture<Integer> enterPatient(@Body PatientDTO patientDTO);
//
//        @GET("list-hoken")
//        CompletableFuture<HokenListDTO> listHoken(@Query("patient-id") int patientId);
//
//        @GET("search-patient-by-name")
//        CompletableFuture<List<PatientDTO>> searchPatientByName(@Query("last-name") String lastName,
//                                                                @Query("first-name") String firstName);
//
//        @GET("search-patient-by-yomi")
//        CompletableFuture<List<PatientDTO>> searchPatientByYomi(@Query("last-name-yomi") String lastNameYomi,
//                                                                @Query("first-name-yomi") String firstNameYomi);
//
//        @GET("list-recently-registered-patients")
//        CompletableFuture<List<PatientDTO>> listRecentlyRegisteredPatients();
//
//        @POST("enter-shahokokuho")
//        CompletableFuture<Integer> enterShahokokuho(@Body ShahokokuhoDTO shahokokuhoDTO);
//
//        @POST("delete-shahokokuho")
//        CompletableFuture<Boolean> deleteShahokokuho(@Body ShahokokuhoDTO shahokokuhoDTO);
//
//        @POST("enter-koukikourei")
//        CompletableFuture<Integer> enterKoukikourei(@Body KoukikoureiDTO koukikoureiDTO);
//
//        @POST("delete-koukikourei")
//        CompletableFuture<Boolean> deleteKoukikourei(@Body KoukikoureiDTO koukikoureiDTO);
//
//        @POST("enter-kouhi")
//        CompletableFuture<Integer> enterKouhi(@Body KouhiDTO kouhiDTO);
//
//        @POST("delete-kouhi")
//        CompletableFuture<Boolean> deleteKouhi(@Body KouhiDTO kouhiDTO);
//
//        @GET("get-visit-meisai")
//        CompletableFuture<MeisaiDTO> getVisitMeisai(@Query("visit-id") int visitId);
//
//        @GET("get-charge")
//        CompletableFuture<ChargeDTO> getCharge(@Query("visit-id") int visitId);
//
//        @GET("find-charge")
//        CompletableFuture<ChargeOptionalDTO> findCharge(@Query("visit-id") int visitId);
//
//        @GET("list-payment")
//        CompletableFuture<List<PaymentDTO>> listPayment(@Query("visit-id") int visitId);
//
//        @GET("list-recent-payment")
//        CompletableFuture<List<PaymentVisitPatientDTO>> listRecentPayment(@Query("n") int n);
//
//        @GET("list-payment-by-patient")
//        CompletableFuture<List<PaymentVisitPatientDTO>> listPaymentByPatient(@Query("patient-id") int patientId,
//                                                                             @Query("n") int n);
//
//        @GET("get-clinic-info")
//        CompletableFuture<ClinicInfoDTO> getClinicInfo();
//
//        @POST("start-visit")
//        CompletableFuture<Integer> startVisit(@Query("patient-id") int patientId);
//
//        @GET("get-visit")
//        CompletableFuture<VisitDTO> getVisit(@Query("visit-id") int visitId);
//
//        @POST("finish-cashier")
//        CompletableFuture<Boolean> finishCashier(@Body PaymentDTO payment);
//
//        @POST("delete-visit-from-reception")
//        CompletableFuture<Boolean> deleteVisitFromReception(@Query("visit-id") int visitId);
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