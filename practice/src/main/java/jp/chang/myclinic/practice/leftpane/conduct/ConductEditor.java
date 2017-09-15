package jp.chang.myclinic.practice.leftpane.conduct;

import jp.chang.myclinic.consts.ConductKind;
import jp.chang.myclinic.dto.ConductFullDTO;
import jp.chang.myclinic.practice.Link;
import jp.chang.myclinic.practice.Service;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

class ConductEditor extends JPanel {

    interface Callback {
        default void onDelete(){}
        default void onClose(){}
    }

    private ConductFullDTO conductFull;
    private Callback callback = new Callback(){};

    ConductEditor(int width, ConductFullDTO conductFull){
        this.conductFull = conductFull;
        setLayout(new MigLayout("insets 0", String.format("[%dpx!]", width), ""));
        add(makeSubmenu(), "wrap");
        add(makeKindArea(), "wrap");
        add(makeCommandBox(), "growx");
    }

    void setCallback(Callback callback){
        this.callback = callback;
    }

    private Component makeSubmenu(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        Link addShinryouLink = new Link("診療行為の追加");
        Link addDrugLink = new Link("薬剤の追加");
        Link addKizaiLink = new Link("器材の追加");
        panel.add(addShinryouLink);
        panel.add(new JLabel("|"));
        panel.add(addDrugLink);
        panel.add(new JLabel("|"));
        panel.add(addKizaiLink);
        return panel;
    }

    private Component makeKindArea(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        JComboBox<String> combo = new JComboBox<>(getConductKindLabels());
        panel.add(new JLabel("種類："));
        panel.add(combo);
        return panel;
    }

    private String[] getConductKindLabels(){
        return Arrays.stream(ConductKind.values()).map(ConductKind::getKanjiRep).toArray(String[]::new);
    }

    private Component makeCommandBox(){
        JPanel panel = new JPanel(new MigLayout("insets 2", "", ""));
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        JButton closeButton = new JButton("閉じる");
        closeButton.addActionListener(event -> callback.onClose());
        Link deleteLink = new Link("削除");
        deleteLink.setCallback(event -> doDelete());
        panel.add(closeButton);
        panel.add(deleteLink);
        return panel;
    }

    private void doDelete(){
        if( !confirm("この処置を削除していいですか？") ){
            return;
        }
        Service.api.deleteConduct(conductFull.conduct.conductId)
                .thenAccept(res -> EventQueue.invokeLater(()-> callback.onDelete()))
                .exceptionally(t -> {
                    t.printStackTrace();
                    EventQueue.invokeLater(() -> {
                        alert(t.toString());
                    });
                    return null;
                });
    }

    private boolean confirm(String message){
        int choice = JOptionPane.showConfirmDialog(this, message, "確認", JOptionPane.YES_NO_OPTION);
        return choice == JOptionPane.YES_OPTION;
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
