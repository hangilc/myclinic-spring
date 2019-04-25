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

    private <T> CompletableFuture<T> get(String path, Consumer<ParamSetter> paramSetter,
                                         GenericType<T> returnType){
        return call(path, paramSetter)
                .get(returnType)
                .toCompletableFuture();
    }

    private <T> CompletableFuture<T> post(String path, Consumer<ParamSetter> paramSetter,
                                          Object body, GenericType<T> returnType){
        return call(path, paramSetter)
                .post(Entity.entity(body, MediaType.APPLICATION_JSON), returnType)
                .toCompletableFuture();
    }

    public CompletableFuture<PatientDTO> getPatient(int patientId) {
        return get("get-patient", setter -> setter.set("patient-id", patientId),
                new GenericType<PatientDTO>(){});
    }

}
