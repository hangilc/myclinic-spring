package jp.chang.myclinic.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.chrono.JapaneseDate;

public class DateTimeUtil {

	public static DateTimeFormatter sqlDateFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
	public static DateTimeFormatter sqlDateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
	public static DateTimeFormatter kanjiFormatter1 = DateTimeFormatter.ofPattern("Gy年M月d日");
	public static DateTimeFormatter kanjiFormatter2 = DateTimeFormatter.ofPattern("Gyy年MM月dd日");

	public static String toKanji(LocalDate date, DateTimeFormatter formatter){
			JapaneseDate jd = JapaneseDate.from(date);
			return jd.format(formatter);
	}

	public static boolean isValidAt(LocalDate at, String validFrom, String validUpto){
		LocalDate validFromDate = LocalDate.parse(validFrom, sqlDateFormatter);
		return validFromDate.compareTo(at) <= 0 &&
			(validUpto == null || "0000-00-00".equals(validUpto) || 
				at.compareTo(LocalDate.parse(validUpto, sqlDateFormatter)) <= 0);
	}
}