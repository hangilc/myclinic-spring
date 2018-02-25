package jp.chang.myclinic.dto;

public class TextVisitDTO {

    public TextDTO text;
    public VisitDTO visit;

    @Override
    public String toString() {
        return "TextVisitDTO{" +
                "text=" + text +
                ", visit=" + visit +
                '}';
    }
}
