package jp.chang.myclinic.practice.leftpane.shinryou;

import jp.chang.myclinic.dto.ShinryouFullDTO;

import javax.swing.*;
import java.awt.*;

class ShinryouDisp extends JEditorPane {

    ShinryouDisp(ShinryouFullDTO shinryouFull, Color background){
        String label = shinryouFull.master.name;
        setContentType("text/plain");
        setText(label);
        setEditable(false);
        setBackground(background);
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    }
}
