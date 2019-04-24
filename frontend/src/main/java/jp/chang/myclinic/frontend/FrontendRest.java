package jp.chang.myclinic.frontend;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.dto.PatientDTO;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FrontendRest {
    static ExecutorService executorService = Executors.newFixedThreadPool(10);
    static Client client = ClientBuilder.newBuilder()
            .executorService(executorService).build();

    CompletableFuture<PatientDTO> getPatient(int patientId){
        ObjectMapper mapper = new ObjectMapper();
        client.register(new JacksonJaxbJsonProvider(mapper,
                JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS));
        return client.target("http://localhost:38080/api")
                .path("get-patient")
                .queryParam("patient-id", patientId)
                .request(MediaType.APPLICATION_JSON)
                .rx()
                .get(new GenericType<PatientDTO>(){})
                .toCompletableFuture();
    }

}
