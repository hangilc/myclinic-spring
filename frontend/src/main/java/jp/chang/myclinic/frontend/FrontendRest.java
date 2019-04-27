package jp.chang.myclinic.frontend;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.CompletionStageRxInvoker;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class FrontendRest /* implements Frontend */{

  static ExecutorService executorService = Executors.newFixedThreadPool(10);

  private static ObjectMapper mapper = new ObjectMapper();

  private static ClientConfig clientConfig = new ClientConfig();

  static {
    clientConfig.executorService(executorService);
    clientConfig.register(
        new JacksonJaxbJsonProvider(mapper, JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS));
  }

  interface ParamSetter {

    void set(String name, Object value);
  }

  private String serverUrl;

  @SuppressWarnings("WeakerAccess")
  public FrontendRest(String serverUrl) {
    this.serverUrl = serverUrl;
  }

  private CompletionStageRxInvoker call(String path, Consumer<ParamSetter> paramSetter) {
    class Local {

      private WebTarget target;
    }
    Local local = new Local();
    local.target = ClientBuilder.newClient(clientConfig).target(serverUrl).path(path);
    paramSetter.accept((name, value) -> local.target = local.target.queryParam(name, value));
    return local.target.request(MediaType.APPLICATION_JSON).rx();
  }

  private <T> CompletableFuture<T> get(
      String path, Consumer<ParamSetter> paramSetter, GenericType<T> returnType) {
    return call(path, paramSetter).get(returnType).toCompletableFuture();
  }

  private <T> CompletableFuture<T> post(
      String path, Consumer<ParamSetter> paramSetter, Object body, GenericType<T> returnType) {
    return call(path, paramSetter)
        .post(Entity.entity(body, MediaType.APPLICATION_JSON), returnType)
        .toCompletableFuture();
  }


}
