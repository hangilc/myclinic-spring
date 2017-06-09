package jp.chang.myclinic;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Created by hangil on 2017/06/09.
 */
public class MeisaiDetailDialog extends JDialog {

    public MeisaiDetailDialog(Window owner){
        super(owner, "明細の詳細");
        setLayout(new MigLayout("fill", "[grow]", "[grow]"));
        add(new MeisaiDetailPane(), "grow");
        pack();
    }

    private static class MeisaiDetailPane extends JPanel {
        MeisaiDetailPane() {
            super(new MigLayout("insets 0, debug", "[grow] []", "[grow]"));
            {
                String text = "long long text for testing purpose";
                JTextArea ta = new JTextArea(text);
                ta.setLineWrap(true);
                add(ta, "grow");
                add(new JLabel("3 x 10 = 30"), "wrap");
            }
            {
                String text = "long long text for testing purpose";
                JTextArea ta = new JTextArea(text);
                ta.setLineWrap(true);
                add(ta, "grow");
                add(new JLabel("3 x 10 = 30"), "wrap");
            }
        }
    }
}
