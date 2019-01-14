package jp.chang.myclinic.util;

import jp.chang.myclinic.util.kanjidate.KanjiDateRepBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.JapaneseDate;
import java.time.chrono.JapaneseEra;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.function.Function;

import static java.time.temporal.ChronoField.YEAR_OF_ERA;

public class DateTimeUtil {

    private static DateTimeFormatter sqlDateFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
    private static DateTimeFormatter sqlDateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
    public static Function<KanjiDateRepBuilder, KanjiDateRepBuilder> kanjiFormatter1 = KanjiDateRepBuilder::format1;
    public static Function<KanjiDateRepBuilder, KanjiDateRepBuilder> kanjiFormatter2 = KanjiDateRepBuilder::format2;
    public static Function<KanjiDateRepBuilder, KanjiDateRepBuilder> kanjiFormatter3 = KanjiDateRepBuilder::format3;
    public static Function<KanjiDateRepBuilder, KanjiDateRepBuilder> kanjiFormatter4 = KanjiDateRepBuilder::format4;
    public static Function<KanjiDateRepBuilder, KanjiDateRepBuilder> kanjiFormatter5 = KanjiDateRepBuilder::format5;
    public static Function<KanjiDateRepBuilder, KanjiDateRepBuilder> kanjiFormatter6 = KanjiDateRepBuilder::format6;
    public static Function<KanjiDateRepBuilder, KanjiDateRepBuilder> kanjiFormatter7 = KanjiDateRepBuilder::format7;

    //	public static DateTimeFormatter kanjiFormatter1 = DateTimeFormatter.ofPattern("Gy年M月d日", Locale.JAPAN);
//	public static DateTimeFormatter kanjiFormatter2 = DateTimeFormatter.ofPattern("Gyy年MM月dd日", Locale.JAPAN);
//	public static DateTimeFormatter kanjiFormatter3 = DateTimeFormatter.ofPattern("Gyy年MM月dd日（E）", Locale.JAPAN);
//	public static DateTimeFormatter kanjiFormatter4 = DateTimeFormatter.ofPattern("HH時mm分", Locale.JAPAN);
//	public static DateTimeFormatter kanjiFormatter5 = DateTimeFormatter.ofPattern("GGGGGy.M.d", Locale.JAPAN);
//	public static DateTimeFormatter kanjiFormatter6 = DateTimeFormatter.ofPattern("H時m分", Locale.JAPAN);
//	public static DateTimeFormatter kanjiFormatter7 = DateTimeFormatter.ofPattern("Gy年M月d日（E）", Locale.JAPAN);
//

    public static String toKanji(LocalDate date,
                                 Function<KanjiDateRepBuilder, KanjiDateRepBuilder> formatter) {
        KanjiDateRepBuilder b = new KanjiDateRepBuilder(date);
        formatter.apply(b);
        return b.build();
    }


	public static String toKanji(LocalDate date){
		return toKanji(date, kanjiFormatter1);
	}

	public static String toKanji(LocalDateTime datetime,
                                 Function<KanjiDateRepBuilder, KanjiDateRepBuilder> dateFormatter,
                                 Function<KanjiDateRepBuilder, KanjiDateRepBuilder> timeFormatter,
                                 String separator){
        KanjiDateRepBuilder b = new KanjiDateRepBuilder(datetime);
        dateFormatter.apply(b);
        b.str(separator);
        timeFormatter.apply(b);
        return b.build();
	}

//	public static boolean isValidAt(LocalDate at, String validFrom, String validUpto){
//		LocalDate validFromDate = LocalDate.parse(validFrom, sqlDateFormatter);
//		return validFromDate.compareTo(at) <= 0 &&
//			(validUpto == null || "0000-00-00".equals(validUpto) ||
//				at.compareTo(LocalDate.parse(validUpto, sqlDateFormatter)) <= 0);
//	}
//
    public static LocalDateTime parseSqlDateTime(String sqlDateTime) {
        return LocalDateTime.parse(sqlDateTime, sqlDateTimeFormatter);
    }

    public static LocalDate parseSqlDate(String sqlDate) {
        if (sqlDate.length() > 10) {
            sqlDate = sqlDate.substring(0, 10);
        }
        return LocalDate.parse(sqlDate, sqlDateFormatter);
    }

	public static String sqlDateToKanji(String sqlDate,
                                        Function<KanjiDateRepBuilder, KanjiDateRepBuilder> formatter){
        LocalDate date = parseSqlDate(sqlDate);
        return toKanji(date, formatter);
	}

//	public static String sqlDateToKanji(String sqlDate){
//        LocalDate date = parseSqlDate(sqlDate);
//        return toKanji(date);
//	}

//	public static String sqlDateToKanjiWithYoubi(String sqlDate) {
//		return sqlDateToKanji(sqlDate, kanjiFormatter3);
//	}
//
//	public static String sqlDateTimeToKanji(String sqlDateTime, DateTimeFormatter dateFormatter, DateTimeFormatter timeFormatter, String separator){
//		LocalDateTime dt = parseSqlDateTime(sqlDateTime);
//		String datePart = toKanji(dt.toLocalDate(), dateFormatter);
//		String timePart = "";
//		if( timeFormatter != null ){
//			timePart = separator + dt.format(timeFormatter);
//		}
//		return datePart + timePart;
//	}

    public static String sqlDateTimeToKanji(
            String sqlDateTime,
            Function<KanjiDateRepBuilder, KanjiDateRepBuilder> dateFormatter,
            Function<KanjiDateRepBuilder, KanjiDateRepBuilder> timeFormatter,
            String separator) {
        LocalDateTime dt = parseSqlDateTime(sqlDateTime);
        return toKanji(dt, dateFormatter, timeFormatter, separator);
    }

    public static String sqlDateTimeToKanji(
            String sqlDateTime,
            Function<KanjiDateRepBuilder, KanjiDateRepBuilder> dateFormatter,
            Function<KanjiDateRepBuilder, KanjiDateRepBuilder> timeFormatter) {
        return sqlDateTimeToKanji(sqlDateTime, dateFormatter, timeFormatter, " ");
    }


//
//	public static String sqlDateTimeToKanji(String sqlDateTime, DateTimeFormatter dateFormatter) {
//		return sqlDateTimeToKanji(sqlDateTime, dateFormatter, null);
//	}
//
//	public static String sqlDateTimeToKanji(String sqlDateTime, DateTimeFormatter dateFormatter, DateTimeFormatter timeFormatter) {
//		return sqlDateTimeToKanji(sqlDateTime, dateFormatter, timeFormatter, " ");
//	}
//
	public static String toSqlDateTime(LocalDateTime at){
		return at.format(sqlDateTimeFormatter);
	}

//	public static JapaneseEra getEra(LocalDate localDate){
//		JapaneseDate jd = JapaneseDate.from(localDate);
//		return jd.getEra();
//	}
//
//	public static int getNen(LocalDate localDate){
//		JapaneseDate jd = JapaneseDate.from(localDate);
//		return jd.get(YEAR_OF_ERA);
//	}
//
//	public static LocalDate warekiToLocalDate(JapaneseEra era, int nen, int month, int day){
//		return LocalDate.ofEpochDay(JapaneseDate.of(era, nen, month, day).toEpochDay());
//	}
//
//	public static boolean isSqlDateUnspecified(String sqldate){
//		return sqldate == null || "0000-00-00".equals(sqldate);
//	}

    public static int calcAge(LocalDate birthday, LocalDate at) {
        return (int) birthday.until(at, ChronoUnit.YEARS);
    }

    public static int calcAge(LocalDate birthday) {
        return calcAge(birthday, LocalDate.now());
    }
}