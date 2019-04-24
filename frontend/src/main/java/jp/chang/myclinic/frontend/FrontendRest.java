package jp.chang.myclinic.frontend;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.dto.PatientDTO;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.*;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class FrontendRest {
    static ExecutorService executorService = Executors.newFixedThreadPool(10);
    static ObjectMapper mapper = new ObjectMapper();
    static ClientConfig clientConfig = new ClientConfig();

    static {
        clientConfig.executorService(executorService);
        clientConfig.register(new JacksonJaxbJsonProvider(mapper,
                JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS));
    }

    interface ParamSetter {
        void set(String name, Object value);
    }

    private CompletionStageRxInvoker call(String path, Consumer<ParamSetter> paramSetter) {
        class Local {
            private WebTarget target;
        }
        Local local = new Local();
        local.target = ClientBuilder.newClient(clientConfig)
                .target("http://localhost:38080/api")
                .path(path);
        paramSetter.accept((name, value) -> {
            local.target = local.target.queryParam(name, value);
        });
        return local.target.request(MediaType.APPLICATION_JSON)
                .rx();
    }

    public CompletableFuture<PatientDTO> getPatient(int patientId) {
        return call("get-patient", setter -> setter.set("patient-id", patientId))
                .get(new GenericType<PatientDTO>(){})
                .toCompletableFuture();
    }

    public CompletableFuture<Void> testVoid() {
        return call("void", setter -> {})
                .post(Entity.entity(null, MediaType.APPLICATION_JSON),
                        new GenericType<Void>(){})
                .toCompletableFuture();
    }

    public CompletableFuture<List<PatientDTO>> testEcho(List<PatientDTO> values){
        return call("echo", setter -> {})
                .post(Entity.entity(values, MediaType.APPLICATION_JSON),
                        new GenericType<List<PatientDTO>>(){})
                .toCompletableFuture();
    }

}
