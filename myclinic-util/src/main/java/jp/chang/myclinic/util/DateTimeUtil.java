package jp.chang.myclinic.util;

import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

	public DateTimeFormatter sqlDateFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
	public DateTimeFormatter sqlDateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");

}