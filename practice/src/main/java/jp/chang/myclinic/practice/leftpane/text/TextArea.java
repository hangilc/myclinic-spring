package jp.chang.myclinic.practice.leftpane.text;

import jp.chang.myclinic.dto.VisitFull2DTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class TextArea extends JPanel {

    public TextArea(VisitFull2DTO fullVisit, int width){
        setLayout(new MigLayout("insets 0", String.format("[%dpx!]", width), ""));
        fullVisit.texts.forEach(text -> {
            TextDispWrapper textDisp = new TextDispWrapper(text, width);
            add(textDisp, "wrap");
        });
    }
}
