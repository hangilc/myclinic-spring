package jp.chang.myclinic.practice.leftpane.text;

import jp.chang.myclinic.dto.TextDTO;

import javax.swing.*;
import java.awt.*;

public class TextDisp extends JEditorPane {

    public TextDisp(TextDTO textDTO, Color background){
        setContentType("text/plain");
        String content = textDTO.content.trim();
        if( content.isEmpty() ){
            content = "(空白)";
        }
        setText(content);
        setEditable(false);
        setBackground(background);
    }

}
