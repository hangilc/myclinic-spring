package jp.chang.myclinic.practice.leftpane;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class WorkArea extends JPanel {

    private Component component;

    public WorkArea(int width, String title){
        super(new MigLayout("insets 0, debug", String.format("[%dpx!]", width), ""));
//        Border border = BorderFactory.createCompoundBorder(
//                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
//                BorderFactory.createEmptyBorder(3, 3, 3, 3)
//        );
        //setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        Insets insets = getInsets();
        int innerWidth = width - insets.left - insets.right;
        add(makeTitle(title), "growx");
    }

    public Component getComponent(){
        return component;
    }

    public void setComponent(Component component, Object layoutConstraints){
        this.component = component;
        add(component, layoutConstraints);
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
