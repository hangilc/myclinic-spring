package jp.chang.myclinic.frontend.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jp.chang.myclinic.frontend.Frontend;
import jp.chang.myclinic.frontend.FrontendRest;
import jp.chang.myclinic.util.DateTimeUtil;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.logging.LoggingFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.CompletionStageRxInvoker;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.logging.Level;

public abstract class FrontendRestBase implements Frontend {

    private static java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(FrontendRest.class.getName());

    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    private ObjectMapper mapper = new ObjectMapper();
    {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(new LocalDateTimeSerializer());
        mapper.registerModule(javaTimeModule);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    private ClientConfig clientConfig = new ClientConfig();

    {
        clientConfig.executorService(executorService);
        clientConfig.register(
                new JacksonJaxbJsonProvider(mapper, JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS));
        clientConfig.register(RestParamConverterProvider.class);
        if( System.getProperty("dump-http") != null ) {
            dumpHttp();
        }
    }

    public void dumpHttp(){
        Feature feature = new LoggingFeature(logger, Level.INFO, null, null);
        clientConfig.register(feature);
    }

    public interface ParamSetter {

        void set(String name, Object value);
    }

    private String serverUrl;

    public FrontendRestBase(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    @Override
    public void shutdown() {
        executorService.shutdown();
    }

    private String formatQueryParam(Object param){
        if( param instanceof LocalDateTime){
            return DateTimeUtil.toSqlDateTime((LocalDateTime)param);
        } else if( param != null ){
            return param.toString();
        } else {
            return null;
        }
    }

    private CompletionStageRxInvoker call(String path, Consumer<ParamSetter> paramSetter) {
        class Local {

            private WebTarget target;
        }
        Local local = new Local();
        local.target = ClientBuilder.newClient(clientConfig).target(serverUrl).path(path);
        paramSetter.accept((name, value) ->
                local.target = local.target.queryParam(name, formatQueryParam(value)));
        return local.target.request(MediaType.APPLICATION_JSON).rx();
    }

    protected <T> CompletableFuture<T> get(
            String path, Consumer<ParamSetter> paramSetter, GenericType<T> returnType) {
        return call(path, paramSetter).get(returnType).toCompletableFuture();
    }

    protected <T> CompletableFuture<T> post(
            String path, Consumer<ParamSetter> paramSetter, Object body, GenericType<T> returnType) {
        return call(path, paramSetter).post(Entity.json(body), returnType).toCompletableFuture();
    }

}
