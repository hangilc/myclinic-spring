package jp.chang.myclinic.practice.javafx.drug;

import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class PrescComposer {

    private static Logger logger = LoggerFactory.getLogger(PrescComposer.class);

    private List<DrugFullDTO> drugs = new ArrayList<>();
    private int index;
    private List<String> lines;
    private String naifukuSpacing;
    private List<String> errors;

    private static DecimalFormat decimalFormat = new DecimalFormat("####.##");

    private static int digitToKanji(int codePoint) {
        switch (codePoint) {
            case '0':
                return '０';
            case '1':
                return '１';
            case '2':
                return '２';
            case '3':
                return '３';
            case '4':
                return '４';
            case '5':
                return '５';
            case '6':
                return '６';
            case '7':
                return '７';
            case '8':
                return '８';
            case '9':
                return '９';
            case '.':
                return '．';
            case '-':
                return 'ー';
            case '(':
                return '（';
            case ')':
                return '）';
            default:
                return codePoint;
        }
    }

    private static String transDigitToKanji(String src) {
        return StringUtil.transliterate(src, PrescComposer::digitToKanji);
    }

    private static Pattern genericManufacturer = Pattern.compile("「[^」]+」");
    private static Pattern breakPoint = Pattern.compile("([ 　]+)|([、・,])");

    private static int MaxPrescChars = 32;

    private String convertDrugNameToPresc(String name) {
        Matcher m = genericManufacturer.matcher(name);
        if (m.find()) {
            name = m.replaceFirst("");
        }
        return name;
    }

    void add(DrugFullDTO drugFull) {
        drugs.add(drugFull);
    }

    List<String> getErrors() {
        return errors;
    }

    String getText() {
        prepare();
        for (DrugFullDTO drugFull : drugs) {
            processDrug(drugFull);
            index += 1;
        }
        return String.join("\n", lines);
    }

    private void prepare() {
        this.index = 1;
        this.lines = new ArrayList<>();
        this.errors = new ArrayList<>();
        this.naifukuSpacing = "";
        lines.add("院外処方");
        lines.add("Ｒｐ）");
    }

    private void processDrug(DrugFullDTO drugFull) {
        DrugCategory category = DrugCategory.fromCode(drugFull.drug.category);
        if (category == null) {
            errors.add("Unknown category: " + drugFull.drug.category);
            return;
        }
        switch (category) {
            case Naifuku: {
                processNaifuku(drugFull);
                break;
            }
            case Tonpuku: {
                processTonpuku(drugFull);
                break;
            }
            case Gaiyou: {
                processGaiyou(drugFull);
                break;
            }
            default: {
                errors.add("Cannot handle category: " + drugFull.drug.category);
                processUnknown(drugFull);
                break;
            }
        }
    }

    private void addLine(String line) {
        if (line.length() > MaxPrescChars) {
            errors.add("長い行を分割しました。");
            breakAndAdd(line, MaxPrescChars);
        } else {
            lines.add(line);
        }
    }

    private enum DelimKind {
        Space,
        Symbol
    }

    private static class Delim {
        DelimKind kind;
        int start;
        int end;

        Delim(DelimKind kind, int start, int end){
            this.kind = kind;
            this.start = start;
            this.end = end;
        }
    }

    private void breakAndAdd(String line, int n) {
        String first = null, rest = null;
        String pre = "　　";
        List<Delim> delims = new ArrayList<>();
        Matcher m = breakPoint.matcher(line);
        while(m.find()){
            String g1 = m.group(1);
            if (g1 != null) {
                int start = m.start(1);
                int end = m.end(1);
                delims.add(new Delim(DelimKind.Space, start, end));
            } else {
                String g2 = m.group(2);
                if (g2 == null) {
                    errors.add("行の分割に失敗しました。");
                } else {
                    int start = m.start(2);
                    int end = m.end(2);
                    delims.add(new Delim(DelimKind.Symbol, start, end));
                }
            }
        }
        if( delims.size() > 0 ){
            boolean done = false;
            int maxRightMargin = 6;
            for(int i=delims.size()-1;i>0;i--){ // i should not be zero
                Delim d = delims.get(i);
                if( d.kind == DelimKind.Space ){
                    first = line.substring(0, d.start);
                    rest = pre + line.substring(d.end);
                    if( first.length() <= n && first.length() >= (n - maxRightMargin) ){
                        done = true;
                        break;
                    }
                } else if( d.kind == DelimKind.Symbol ){
                    first = line.substring(0, d.end);
                    rest = pre + line.substring(d.end);
                    if( first.length() <= n && first.length() >= (n - maxRightMargin) ){
                        done = true;
                        break;
                    }
                } else {
                    errors.add("Unknown delimiter: " + d);
                    break;
                }
            }
            if( !done ){
                first = line.substring(0, n);
                rest = pre + line.substring(n);
            }
        } else {
            first = line.substring(0, n);
            rest = pre + line.substring(n);
        }
        lines.add(first);
        if (rest.length() <= n) {
            lines.add(rest);
        } else {
            breakAndAdd(rest, n);
        }
    }

    private void processNaifuku(DrugFullDTO drugFull) {
        String pad = "";
        String drugName = convertDrugNameToPresc(drugFull.master.name);
        int regularNameMax = 18;
        int nameSize = drugName.length();
        if( nameSize < regularNameMax ){
            pad = "　".repeat(regularNameMax - nameSize);
        }
        String line = String.format("%d）%s%s　%s%s",
                index,
                drugName,
                pad,
                decimalFormat.format(drugFull.drug.amount),
                drugFull.master.unit);
        line = transDigitToKanji(line);
        String line2 = String.format("　　%s %d日分", drugFull.drug.usage, drugFull.drug.days);
        line2 = transDigitToKanji(line2);
        addLine(line);
        addLine(line2);
    }

    private void processTonpuku(DrugFullDTO drugFull) {
        String line = String.format("%d）%s １回%s%s",
                index,
                convertDrugNameToPresc(drugFull.master.name),
                decimalFormat.format(drugFull.drug.amount),
                drugFull.master.unit);
        line = transDigitToKanji(line);
        String line2 = String.format("　　%s %d回分", drugFull.drug.usage, drugFull.drug.days);
        line2 = transDigitToKanji(line2);
        addLine(line);
        addLine(line2);
    }

    private void processGaiyou(DrugFullDTO drugFull) {
        String line = String.format("%d）%s %s%s",
                index,
                convertDrugNameToPresc(drugFull.master.name),
                decimalFormat.format(drugFull.drug.amount),
                drugFull.master.unit);
        line = transDigitToKanji(line);
        String line2 = String.format("　　%s", drugFull.drug.usage);
        line2 = transDigitToKanji(line2);
        addLine(line);
        addLine(line2);
    }

    private void processUnknown(DrugFullDTO drugFull) {
        String line = String.format("%d）%s %s%s",
                index,
                convertDrugNameToPresc(drugFull.master.name),
                decimalFormat.format(drugFull.drug.amount),
                drugFull.master.unit);
        line = transDigitToKanji(line);
        String line2 = String.format("　　%s", drugFull.drug.usage);
        line2 = transDigitToKanji(line2);
        addLine(line);
        addLine(line2);
    }
}
