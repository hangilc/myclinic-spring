package jp.chang.myclinic.pharma.swing;

import javax.swing.*;
import java.awt.*;

/**
 * Created by hangil on 2017/06/12.
 */
public class BlankIcon implements Icon {
    private int width;
    private int height;

    public BlankIcon(int width, int height){
        this.width = width;
        this.height = height;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        // nop
    }

    @Override
    public int getIconWidth() {
        return width;
    }

    @Override
    public int getIconHeight() {
        return height;
    }
}
