package jp.chang.myclinic.mastermap.next;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeMapEntry implements Comparable<CodeMapEntry> {
    private int oldCode;
    private int newCode;
    private LocalDate validFrom;
    private String comment;

    CodeMapEntry(int oldCode, int newCode, LocalDate at, String comment){
        this.oldCode = oldCode;
        this.newCode = newCode;
        this.validFrom = at;
        this.comment = comment;
    }

    CodeMapEntry(CodeMapEntry src){
        this(src.oldCode, src.newCode, src.validFrom, src.comment);
    }

    public int getOldCode() {
        return oldCode;
    }

    public int getNewCode() {
        return newCode;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public String getComment() {
        return comment;
    }

    int apply(int code, LocalDate at){
        if( code == oldCode && validFrom.compareTo(at) <= 0 ){
            return newCode;
        } else {
            return code;
        }
    }

    @Override
    public int compareTo(CodeMapEntry e) {
        return validFrom.compareTo(e.validFrom);
    }

    private static Pattern pattern = Pattern.compile("^(?:Y|S|K),(\\d{9}),(\\d{4}-\\d{2}-\\d{2}),(\\d{9})(\\s+(.*))?");

    static CodeMapEntry parse(String src){
        //example: "Y,611140694,2012-04-01,620098801 ロキソニン錠６０ｍｇ";
        Matcher matcher = pattern.matcher(src);
        if( !matcher.matches() ){
            throw new RuntimeException("invalid master map entry: " + src);
        }
        String oldCode = matcher.group(1);
        String validFrom = matcher.group(2);
        String newCode = matcher.group(3);
        String comment = matcher.group(5);
        int oldCodeValue = Integer.parseInt(oldCode);
        int newCodeValue = Integer.parseInt(newCode);
        LocalDate validFromValue = stringToLocalDate(validFrom);
        return new CodeMapEntry(oldCodeValue, newCodeValue, validFromValue, comment);
    }

    private static LocalDate stringToLocalDate(String src){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        formatter.withResolverStyle(ResolverStyle.STRICT);
        return LocalDate.parse(src, formatter);
    }

}
