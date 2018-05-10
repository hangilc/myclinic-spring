package jp.chang.myclinic.rcpt.builder;

import jp.chang.myclinic.dto.VisitDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class VisitModifier {

    //private static Logger logger = LoggerFactory.getLogger(VisitModifier.class);
    private VisitDTO visit;
    private static DateTimeFormatter sqlDateTimeFormatter =
            DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");

    VisitModifier(VisitDTO visit) {
        this.visit = visit;
    }

    public VisitModifier setVisitedAt(LocalDate date){
        return setVisitedAt(date, 10, 20);
    }

    public VisitModifier setVisitedAt(LocalDate date, int hour, int minute){
        LocalDateTime at = LocalDateTime.of(date, LocalTime.of(hour, minute));
        visit.visitedAt = at.format(sqlDateTimeFormatter);
        return this;
    }

}
