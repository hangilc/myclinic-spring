package jp.chang.myclinic.practice;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class WorkArea extends JPanel {

    private Component component;

    public WorkArea(int width, String title){
        super(new FixedWidthLayout(width));
        Border border = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(3, 3, 3, 3)
        );
        setBorder(border);
        add(makeTitle(title));
    }

    public Component getComponent(){
        return component;
    }

    private JComponent makeTitle(String text){
        JLabel title = new JLabel(text);
        title.setBackground(new Color(0xdd, 0xdd, 0xdd));
        title.setOpaque(true);
        Font font = title.getFont().deriveFont(Font.BOLD);
        title.setFont(font);
        title.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        return title;
    }

}
