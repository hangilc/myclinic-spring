package jp.chang.myclinic.practice.leftpane;

import jp.chang.myclinic.practice.Link;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

class PatientManip extends JPanel {

    interface Callback {
        default void onCashier(){}
        default void onFinishPatient(){}
        default void onRefer(){}
    }

    private static Cursor handCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);

    private Callback callback = new Callback(){};

    PatientManip(){
        setLayout(new MigLayout("insets 0", "", ""));
        JButton cashierButton = new JButton("会計");
        cashierButton.addActionListener(evt -> callback.onCashier());
        JButton endPatientButton = new JButton("患者終了");
        endPatientButton.addActionListener(event -> callback.onFinishPatient());
        Link searchTextLink = new Link("文章検索");
        Link makeReferLink = new Link("紹介状作成");
        makeReferLink.setCallback(evt -> callback.onRefer());
        add(cashierButton, "sizegroup btn");
        add(endPatientButton, "sizegroup btn");
        add(new JLabel("|" ));
        add(searchTextLink);
        add(new JLabel("|" ));
        add(makeReferLink);
    }

    void setCallback(Callback callback){
        this.callback = callback;
    }

}
