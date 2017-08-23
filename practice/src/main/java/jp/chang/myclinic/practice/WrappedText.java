package jp.chang.myclinic.practice;

import javax.swing.*;
import java.awt.*;

public class WrappedText extends JEditorPane {

    private static Color background;

    static {
        EventQueue.invokeLater(() -> {
            background = new JPanel().getBackground();
        });
    }

    public WrappedText(int width, String text) {
        setContentType("text/plain");
        setSize(width, Integer.MAX_VALUE);
        setText(text);
        setEditable(false);
        setBorder(null);
        setBackground(background);
    }
}
