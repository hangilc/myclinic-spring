package jp.chang.myclinic.practice.leftpane.conduct;

import jp.chang.myclinic.dto.ConductFullDTO;
import jp.chang.myclinic.practice.Link;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

class ConductEditor extends JPanel {

    interface Callback {
        default void onClose(){}
    }

    private Callback callback = new Callback(){};

    ConductEditor(int width, ConductFullDTO conductFull){
        setLayout(new MigLayout("insets 0", String.format("[%dpx!]", width), ""));
        add(makeCommandBox(), "growx");
    }

    void setCallback(Callback callback){
        this.callback = callback;
    }

    private Component makeCommandBox(){
        JPanel panel = new JPanel(new MigLayout("insets 2", "", ""));
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        JButton closeButton = new JButton("閉じる");
        closeButton.addActionListener(event -> callback.onClose());
        Link deleteLink = new Link("削除");
        panel.add(closeButton);
        panel.add(deleteLink);
        return panel;
    }
}
