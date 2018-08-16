package jp.chang.myclinic.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NormalizedDrugUsage {

    private static Pattern trimPattern = Pattern.compile("^[ 　]+|[ 　]+$");
    private static Pattern timesPattern = Pattern.compile("分([0-9０-９]+)(.*)");
    private static Pattern unevenPattern = Pattern.compile(".*([(（]([- 　0-9０-９.．ー－]+)[)）]).*");
    private static Pattern mealsPattern = Pattern.compile("([朝昼夕]+)食(.)");

    private List<String> parts = new ArrayList<>();
    private Integer times = null;
    private String usageWithoutTimes;

    public NormalizedDrugUsage(String usage) {
        if( usage != null ){
            usage = trimSpaces(usage);
            Matcher matcher = timesPattern.matcher(usage);
            if( matcher.matches() ){
                this.times = Integer.parseInt(kanjiStringToDigitString(matcher.group(1)));
                this.usageWithoutTimes = trimSpaces(matcher.group(2));
            } else {
                this.usageWithoutTimes = usage;
            }
            parts.addAll(extractTimingParts(usageWithoutTimes));
        }
    }

    public List<String> getParts(){
        return Collections.unmodifiableList(parts);
    }

    Integer getTimes(){
        return times;
    }

    String getUsageWithoutTimes(){
        return usageWithoutTimes;
    }

    private List<String> extractTimingParts(String usage){
        List<String> parts = new ArrayList<>();
        for(String part: usage.split("[,、]")){
            part = part.trim();
            if( part.startsWith("毎食") ){
                String suffix = part.substring(2, 3);
                parts.add("朝食" + suffix);
                parts.add("昼食" + suffix);
                parts.add("夕食" + suffix);
            } else {
                Matcher matcher = mealsPattern.matcher(usage);
                if( matcher.matches() ){
                    String suffix = matcher.group(2);
                    matcher.group(1).chars().forEach(ch -> {
                        parts.add(String.format("%c食%s", ch, suffix));
                    });
                } else {
                    parts.add(part);
                }
            }
        }
        return parts;
    }

    private String trimSpaces(String s){
        Matcher matcher = trimPattern.matcher(s);
        if( matcher.find() ){
            return matcher.replaceAll("");
        } else {
            return s;
        }
    }

    private int digitToKanji(int codePoint){
        switch(codePoint){
            case '0': return '０';
            case '1': return '１';
            case '2': return '２';
            case '3': return '３';
            case '4': return '４';
            case '5': return '５';
            case '6': return '６';
            case '7': return '７';
            case '8': return '８';
            case '9': return '９';
            default: return codePoint;
        }
    }

    private int kanjiToDigit(int codePoint){
        switch(codePoint){
            case '０': return '0';
            case '１': return '1';
            case '２': return '2';
            case '３': return '3';
            case '４': return '4';
            case '５': return '5';
            case '６': return '6';
            case '７': return '7';
            case '８': return '8';
            case '９': return '9';
            default: return codePoint;
        }
    }

    private int numToKanji(int codePoint){
        switch(codePoint){
            case '.': return '．';
            default: return digitToKanji(codePoint);
        }
    }

    private String numberStringToKanjiString(String s){
        return s.codePoints().map(this::numToKanji)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    private String kanjiStringToDigitString(String s){
        return s.codePoints().map(this::numToKanji)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }


}
