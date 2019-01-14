package jp.chang.myclinic.util;

import jp.chang.myclinic.util.kanjidate.KanjiDateRepBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.function.Function;

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
        if( dateFormatter != null ) {
            dateFormatter.apply(b);
        }
        b.str(separator);
        if( timeFormatter != null ) {
            timeFormatter.apply(b);
        }
        return b.build();
	}

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

	public static String toSqlDateTime(LocalDateTime at){
		return at.format(sqlDateTimeFormatter);
	}

    public static int calcAge(LocalDate birthday, LocalDate at) {
        return (int) birthday.until(at, ChronoUnit.YEARS);
    }

    public static int calcAge(LocalDate birthday) {
        return calcAge(birthday, LocalDate.now());
    }
}