package jp.chang.myclinic.mastermap2;

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

    @Override
    public String toString() {
        return "CodeMapEntry{" +
                "oldCode=" + oldCode +
                ", newCode=" + newCode +
                ", validFrom=" + validFrom +
                ", comment='" + comment + '\'' +
                '}';
    }
}
