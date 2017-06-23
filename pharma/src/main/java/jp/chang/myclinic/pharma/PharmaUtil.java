package jp.chang.myclinic.pharma;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;

public class PharmaUtil {

    public static JLabel makeLink(String text){
        JLabel label = new JLabel(text);
        label.setForeground(Color.BLUE);
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return label;
    }
}
