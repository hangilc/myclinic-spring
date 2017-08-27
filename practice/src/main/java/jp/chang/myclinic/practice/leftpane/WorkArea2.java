package jp.chang.myclinic.practice.leftpane;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class WorkArea2<T extends JComponent> extends JPanel {

    private T component;

    public WorkArea2(String title, T component){
        super(new MigLayout("", "[grow]", ""));
        this.component = component;
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        add(makeTitle(title), "growx, wrap");
        add(component, "growx");
    }

    public T getComponent(){
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
