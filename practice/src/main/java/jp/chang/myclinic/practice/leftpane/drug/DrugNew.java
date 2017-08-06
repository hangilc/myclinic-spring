package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.practice.Link;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

class DrugNew extends JPanel {

    DrugNew(int patientId){
        DrugSearch drugSearch = new DrugSearch(patientId);
        setLayout(new MigLayout("", "[grow]", ""));
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
        DrugInfoNew drugInfoPane = new DrugInfoNew();
        add(new JLabel("新規処方の入力"), "growx, wrap");
        add(drugInfoPane, "growx, wrap");
        add(makeCommandBox(), "growx, wrap");
        add(drugSearch, "growx");
    }

    private JComponent makeCommandBox(){
        JButton enterButton = new JButton("入力");
        JButton closeButton = new JButton("閉じる");
        Link clearLink = new Link("クリア");
        JPanel box = new JPanel(new MigLayout("", "", ""));
        box.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        box.add(enterButton);
        box.add(closeButton);
        box.add(clearLink);
        return box;
    }
}
