package jp.chang.myclinic;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import jp.chang.myclinic.dto.*;

import retrofit2.Retrofit;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.adapter.java8.Java8CallAdapterFactory;

class Service {
	public static interface ServerAPI {
		@GET("list-wqueue-full")
		CompletableFuture<List<WqueueFullDTO>> listWqueue();
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

	static public PatientDTO getPatient(int patientId) throws IOException {
		return null;
	}

	static public List<PatientDTO> searchPatientByName(String lastName, String firstName) throws IOException {
		return null;
	}

}