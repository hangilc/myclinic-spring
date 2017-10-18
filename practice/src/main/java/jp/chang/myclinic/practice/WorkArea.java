package jp.chang.myclinic.practice;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class WorkArea extends JPanel {

    private static int borderWidth = 2;
    private static int insetWidth = 3;

    private static int calcInnerColumnWidth(int width){
        return width - borderWidth * 2 - insetWidth * 2;
    }

    private static MigLayout createLayout(int width){
        return new MigLayout(
                String.format("insets %d", insetWidth),
                String.format("[%dpx!]", calcInnerColumnWidth(width)),
                ""
        );
    }

    private int width;
    private Component component;

    public WorkArea(int width, String title){
        super(createLayout(width));
        this.width = width;
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        add(makeTitle(title), "grow, wrap");
    }

    public int getInnerColumnWidth(){
        return calcInnerColumnWidth(width);
    }

    public Component getComponent(){
        return component;
    }

    public void setComponent(Component component){
        this.component = component;
        add(component, "growx");
    }

    private JComponent makeTitle(String text){
        JLabel title = new JLabel(text);
        title.setBackground(new Color(0xdd, 0xdd, 0xdd));
        title.setOpaque(true);
        Font font = title.getFont().deriveFont(Font.BOLD);
        title.setFont(font);
        title.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        //title.setSize(getInnerColumnWidth(), Integer.MAX_VALUE);
        return title;
    }

}
