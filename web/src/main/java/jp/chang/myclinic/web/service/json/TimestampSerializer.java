package jp.chang.myclinic.web.service.json;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimestampSerializer extends JsonSerializer<Timestamp> {

	@Override
	public void serialize(Timestamp timestamp, JsonGenerator jsonGenerator,
		SerializerProvider serializerProvider)
		throws IOException, JsonProcessingException {
		LocalDateTime time = timestamp.toLocalDateTime();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
		String output = time.format(formatter);
		jsonGenerator.writeString(output);	
	}

}


