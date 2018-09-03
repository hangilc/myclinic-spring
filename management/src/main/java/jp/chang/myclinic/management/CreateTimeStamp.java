package jp.chang.myclinic.management;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CreateTimeStamp {

    public static void main(String[] args) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("uuuuMMdd-HHmmss");
        LocalDateTime t = LocalDateTime.now();
        System.out.println(t.format(format));
    }

}
