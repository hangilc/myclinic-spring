package jp.chang.myclinic.practice.leftpane.shinryou;

import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.practice.MainExecContext;

import javax.swing.*;
import java.awt.*;

class ShinryouDisp extends JEditorPane {

    ShinryouDisp(ShinryouFullDTO shinryouFull, Color background, int width, MainExecContext mainExecContext){
        String label = shinryouFull.master.name;
        setContentType("text/plain");
        setSize(width, Integer.MAX_VALUE);
        setText(label);
        setEditable(false);
        setBackground(background);
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    }
}
