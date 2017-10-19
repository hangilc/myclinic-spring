package jp.chang.myclinic.practice.rightpane.disease.addpane;

import jp.chang.myclinic.practice.WrappedText;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

class Notice extends JPanel {
    Notice(int width, String message){
        setLayout(new MigLayout("insets 4", "", ""));
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        WrappedText text = new WrappedText(width - 8, message);
        add(text);
    }
}
