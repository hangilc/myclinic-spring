package jp.chang.myclinic.backendserver;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.backenddb.DbBackendService;
import jp.chang.myclinic.support.SupportSet;
import jp.chang.myclinic.util.DateTimeUtil;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;
import java.io.IOException;
import java.time.LocalDateTime;

@ApplicationPath("api")
class JerseyConfig extends ResourceConfig {

    private static class Binder extends AbstractBinder {

        private DbBackend dbBackend;
        private DbBackendService dbBackendService;
        private SupportSet ss;

        private Binder(DbBackend dbBackend, SupportSet ss){
            this.dbBackend = dbBackend;
            this.dbBackendService = new DbBackendService(dbBackend);
            this.ss = ss;
        }

        @Override
        protected void configure() {
            bind(dbBackend).to(DbBackend.class);
            bind(dbBackendService).to(DbBackendService.class);
            bind(ss).to(SupportSet.class);
        }
    }

    private static class JsonLocalDateTimeSerializer extends StdSerializer<LocalDateTime> {

        JsonLocalDateTimeSerializer() {
            super(LocalDateTime.class);
        }

        @Override
        public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator,
                              SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(DateTimeUtil.toSqlDateTime(localDateTime));
        }

    }

    private static ObjectMapper mapper = new ObjectMapper();
    static {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(new JsonLocalDateTimeSerializer());
        mapper.registerModule(javaTimeModule);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    JerseyConfig(DbBackend dbBackend, SupportSet ss) {
        register(RestServer.class);
        register(RestParamConverterProvider.class);
        register(new JacksonJaxbJsonProvider(mapper, JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS));
        register(new Binder(dbBackend, ss));
    }
}
