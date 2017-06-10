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
        setLayout(new MigLayout("", "[]", "[]"));
        add(new MeisaiDetailPane(), "");
        pack();
    }

    private static class MeisaiDetailPane extends JPanel {
        MeisaiDetailPane() {
            super(new MigLayout("insets 0", "[] []", "[]"));
            {
                String text = "明細の詳細明細の詳細明細の詳細明細の詳細明細の詳細明細の詳細";
                JTextArea ta = new JTextArea(text);
                ta.setLineWrap(true);
                ta.setBackground(getBackground());
                add(ta, "w 200");
                add(new JLabel("3 x 10 = 30"), "wrap");
            }
        }
    }
}

