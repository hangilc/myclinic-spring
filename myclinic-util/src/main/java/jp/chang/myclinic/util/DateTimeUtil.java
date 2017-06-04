package jp.chang.myclinic.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.JapaneseDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateTimeUtil {

	public static DateTimeFormatter sqlDateFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
	public static DateTimeFormatter sqlDateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
	public static DateTimeFormatter kanjiFormatter1 = DateTimeFormatter.ofPattern("Gy年M月d日");
	public static DateTimeFormatter kanjiFormatter2 = DateTimeFormatter.ofPattern("Gyy年MM月dd日");
	public static DateTimeFormatter kanjiFormatter3 = DateTimeFormatter.ofPattern("Gyy年MM月dd日（E）");

	public static String toKanji(LocalDate date, DateTimeFormatter formatter){
			JapaneseDate jd = JapaneseDate.from(date);
			return jd.format(formatter);
	}

	public static String toKanji(LocalDateTime dateTime, DateTimeFormatter formatter){
			JapaneseDate jd = JapaneseDate.from(dateTime);
			return jd.format(formatter);
	}

	public static boolean isValidAt(LocalDate at, String validFrom, String validUpto){
		LocalDate validFromDate = LocalDate.parse(validFrom, sqlDateFormatter);
		return validFromDate.compareTo(at) <= 0 &&
			(validUpto == null || "0000-00-00".equals(validUpto) || 
				at.compareTo(LocalDate.parse(validUpto, sqlDateFormatter)) <= 0);
	}

	public static LocalDateTime parseSqlDateTime(String sqlDateTime){
		return LocalDateTime.parse(sqlDateTime, sqlDateTimeFormatter);
	}

	public static LocalDate parseSqlDate(String sqlDate){
		return LocalDate.parse(sqlDate, sqlDateFormatter);
	}

	public static String formatSqlDate(String sqlDate){
		return formatSqlDate(sqlDate, kanjiFormatter1);
	}

	public static String formatSqlDate(String sqlDate, DateTimeFormatter formatter){
		return toKanji(parseSqlDate(sqlDate), formatter);
	}

	public static String formatSqlDateTime(String sqlDateTime, DateTimeFormatter formatter){
		return toKanji(parseSqlDateTime(sqlDateTime), formatter);
	}

	public static int calcAge(LocalDate birthday, LocalDate at){
		return (int)birthday.until(at, ChronoUnit.YEARS);
	}

	public static int calcAge(LocalDate birthday){
		return calcAge(birthday, LocalDate.now());
	}
}