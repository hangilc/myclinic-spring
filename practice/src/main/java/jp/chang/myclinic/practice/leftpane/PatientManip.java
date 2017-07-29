package jp.chang.myclinic.practice.leftpane;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class PatientManip extends JPanel {

    private static Cursor handCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);

    public PatientManip(){
        setLayout(new MigLayout("", "", ""));
        JButton cashierButton = new JButton("会計");
        JButton endPatientButton = new JButton("患者終了");
        JComponent searchTextLink = makeLink("文章検索");
        JComponent makeReferLink = makeLink("紹介状作成");
        add(cashierButton, "sizegroup btn");
        add(endPatientButton, "sizegroup btn");
        add(new JLabel("|" ));
        add(searchTextLink);
        add(makeReferLink);
    }

    private JComponent makeLink(String label){
        JLabel link = new JLabel(label);
        link.setForeground(Color.BLUE);
        link.setOpaque(true);
        link.setCursor(handCursor);
        return link;
    }
}
