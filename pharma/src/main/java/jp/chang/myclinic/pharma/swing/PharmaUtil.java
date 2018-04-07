package jp.chang.myclinic.pharma.swing;

import javax.swing.*;
import java.awt.*;

public class PharmaUtil {

    public static JLabel makeLink(String text){
        JLabel label = new JLabel(text);
        label.setForeground(Color.BLUE);
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return label;
    }
}
