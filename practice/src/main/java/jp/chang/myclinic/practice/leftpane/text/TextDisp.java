package jp.chang.myclinic.practice.leftpane.text;

import jp.chang.myclinic.dto.TextDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class TextDisp extends JEditorPane {

    TextDisp(TextDTO textDTO, Color background, int width){
        setContentType("text/plain");
        String content = textDTO.content.trim();
        if( content.isEmpty() ){
            content = "(空白)";
        }
        setSize(width, Integer.MAX_VALUE);
        setText(content);
        setEditable(false);
        setBackground(background);
        setBorder(null);
        addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {

            }
        });
    }

}
