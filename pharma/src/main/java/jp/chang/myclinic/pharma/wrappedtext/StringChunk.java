package jp.chang.myclinic.pharma.wrappedtext;

import java.awt.*;

public class StringChunk {
    public String text;
    public Rectangle rect;

    public StringChunk(String text, Rectangle rect){
        this.text = text;
        this.rect = rect;
    }
}
