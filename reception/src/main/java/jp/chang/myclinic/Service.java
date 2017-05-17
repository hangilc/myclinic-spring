package jp.chang.myclinic;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import jp.chang.myclinic.dto.*;

import retrofit2.Retrofit;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Body;
import retrofit2.http.Query;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.adapter.java8.Java8CallAdapterFactory;

class Service {
    public static interface ServerAPI {
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

        @POST("enter-kouhi")
        CompletableFuture<Integer> enterKouhi(@Body KouhiDTO kouhiDTO);

        @POST("delete-kouhi")
        CompletableFuture<Boolean> deleteKouhi(@Body KouhiDTO kouhiDTO);

		@GET("get-visit-meisai")
		CompletableFuture<MeisaiDTO> getVisitMeisai(@Query("visit-id") int visitId);

		@GET("get-charge")
		CompletableFuture<ChargeDTO> getCharge(@Query("visit-id") int visitId);

		@GET("find-charge")
		CompletableFuture<ChargeDTO> findCharge(@Query("visit-id") int visitId);

		@GET("list-payment")
		CompletableFuture<List<PaymentDTO>> listPayment(@Query("visit-id") int visitId);
	}

    public static ServerAPI api; 

    public static void setServerUrl(String serverUrl){
        Retrofit server = new Retrofit.Builder()
            .baseUrl(serverUrl)
            .addConverterFactory(JacksonConverterFactory.create())
            .addCallAdapterFactory(Java8CallAdapterFactory.create())
            .build();
        api = server.create(ServerAPI.class);
    }

}