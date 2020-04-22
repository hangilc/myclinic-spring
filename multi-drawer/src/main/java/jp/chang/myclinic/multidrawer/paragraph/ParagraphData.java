package jp.chang.myclinic.multidrawer.paragraph;

import jp.chang.myclinic.drawer.PaperSize;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ParagraphData {

    private final List<String> lines = new ArrayList<>();

    public ParagraphData(){

    }

    public void add(String lines){
        Collections.addAll(this.lines, splitToLines(lines));
    }

    public void add(List<String> lines){
        lines.forEach(this::add);
    }

    public void add(String line1, String line2, String... lines){
        add(line1);
        add(line2);
        for(String line: lines){
            add(line);
        }
    }

    public void addBlankLine(){
        this.lines.add("");
    }

    public void addBlankLines(int count){
        for(int i=0;i<count;i++){
            addBlankLine();
        }
    }

    public List<String> getLines(){
        return lines;
    }

    private String[] splitToLines(String line){
        if( line == null ){
            return new String[]{};
        } else {
            return line.split("\\r\\n|\\n|\\r");
        }
    }

}
