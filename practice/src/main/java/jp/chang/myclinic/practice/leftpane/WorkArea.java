package jp.chang.myclinic.practice.leftpane;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class WorkArea extends JPanel {

    public WorkArea(String title, JComponent pane){
        super(new MigLayout("", "[grow]", ""));
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        add(makeTitle(title), "growx, wrap");
        add(pane, "growx");
    }

    private JComponent makeTitle(String text){
        JLabel title = new JLabel(text);
        title.setBackground(new Color(0xdd, 0xdd, 0xdd));
        title.setOpaque(true);
        Font font = title.getFont().deriveFont(Font.BOLD);
        title.setFont(font);
        return title;
    }

}
