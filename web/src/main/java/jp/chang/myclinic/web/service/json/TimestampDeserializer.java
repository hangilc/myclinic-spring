package jp.chang.myclinic.web.service.json;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class TimestampDeserializer extends JsonDeserializer<Timestamp> {

	@Override
	public Timestamp deserialize(JsonParser jsonParser, DeserializationContext context)
		throws IOException, JsonProcessingException {
		String data = jsonParser.getText();
		DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
		builder.parseStrict();
		DateTimeFormatter formatter = builder.toFormatter();
		LocalDateTime dateTime = LocalDateTime.parse(data, formatter);
		return Timestamp.valueOf(dateTime);
	}

}

