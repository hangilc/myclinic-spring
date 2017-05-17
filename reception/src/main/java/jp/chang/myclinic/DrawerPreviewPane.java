package jp.chang.myclinic;

import jp.chang.myclinic.drawer.Op;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Created by hangil on 2017/05/17.
 */
public class DrawerPreviewPane extends JPanel {

    private List<Op> ops;

    public DrawerPreviewPane(List<Op> ops){
        this.ops = ops;
    }

    @Override
    public void paintComponent(Graphics g1){
        Graphics2D g = (Graphics2D) g1;

    }
}
