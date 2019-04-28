package jp.chang.myclinic.frontend.rest;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import jp.chang.myclinic.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;

public class LocalDateTimeSerializer extends StdSerializer<LocalDateTime> {

    public LocalDateTimeSerializer() {
        super(LocalDateTime.class);
    }

    @Override
    public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        System.out.println("Serializing LocalDateTime");
        jsonGenerator.writeString(DateTimeUtil.toSqlDateTime(localDateTime));
    }

}
