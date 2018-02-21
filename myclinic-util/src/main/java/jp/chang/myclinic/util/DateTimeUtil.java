package jp.chang.myclinic.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.JapaneseDate;
import java.time.chrono.JapaneseEra;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static java.time.temporal.ChronoField.YEAR_OF_ERA;

public class DateTimeUtil {

	public static DateTimeFormatter sqlDateFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
	public static DateTimeFormatter sqlDateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
	public static DateTimeFormatter kanjiFormatter1 = DateTimeFormatter.ofPattern("Gy年M月d日");
	public static DateTimeFormatter kanjiFormatter2 = DateTimeFormatter.ofPattern("Gyy年MM月dd日");
	public static DateTimeFormatter kanjiFormatter3 = DateTimeFormatter.ofPattern("Gyy年MM月dd日（E）");
	public static DateTimeFormatter kanjiFormatter4 = DateTimeFormatter.ofPattern("HH時mm分");
	public static DateTimeFormatter kanjiFormatter5 = DateTimeFormatter.ofPattern("GGGGGy.M.d");

	public static String toKanji(LocalDate date, DateTimeFormatter formatter){
			JapaneseDate jd = JapaneseDate.from(date);
			return jd.format(formatter);
	}

	public static String toKanji(LocalDate date){
		return toKanji(date, kanjiFormatter1);
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

	public static String sqlDateToKanji(String sqlDate){
		return sqlDateToKanji(sqlDate, kanjiFormatter1);
	}

	public static String sqlDateToKanjiWithYoubi(String sqlDate) {
		return sqlDateToKanji(sqlDate, kanjiFormatter3);
	}

	public static String sqlDateToKanji(String sqlDate, DateTimeFormatter formatter){
		return toKanji(parseSqlDate(sqlDate), formatter);
	}

	public static String sqlDateTimeToKanji(String sqlDateTime, DateTimeFormatter dateFormatter, DateTimeFormatter timeFormatter, String separator){
		LocalDateTime dt = parseSqlDateTime(sqlDateTime);
		String datePart = toKanji(dt.toLocalDate(), dateFormatter);
		String timePart = "";
		if( timeFormatter != null ){
			timePart = separator + dt.format(timeFormatter);
		}
		return datePart + timePart;
	}

	public static String sqlDateTimeToKanji(String sqlDateTime, DateTimeFormatter dateFormatter) {
		return sqlDateTimeToKanji(sqlDateTime, dateFormatter, null);
	}

	public static String sqlDateTimeToKanji(String sqlDateTime, DateTimeFormatter dateFormatter, DateTimeFormatter timeFormatter) {
		return sqlDateTimeToKanji(sqlDateTime, dateFormatter, timeFormatter, " ");
	}

	public static String toSqlDateTime(LocalDateTime at){
		return at.format(sqlDateTimeFormatter);
	}

	public static JapaneseEra getEra(LocalDate localDate){
		JapaneseDate jd = JapaneseDate.from(localDate);
		return jd.getEra();
	}

	public static int getNen(LocalDate localDate){
		JapaneseDate jd = JapaneseDate.from(localDate);
		return jd.get(YEAR_OF_ERA);
	}

	public static LocalDate warekiToLocalDate(JapaneseEra era, int nen, int month, int day){
		return LocalDate.ofEpochDay(JapaneseDate.of(era, nen, month, day).toEpochDay());
	}

	public static boolean isSqlDateUnspecified(String sqldate){
		return sqldate == null || "0000-00-00".equals(sqldate);
	}

	public static int calcAge(LocalDate birthday, LocalDate at){
		return (int)birthday.until(at, ChronoUnit.YEARS);
	}

	public static int calcAge(LocalDate birthday){
		return calcAge(birthday, LocalDate.now());
	}
}