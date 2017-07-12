package jp.chang.myclinic.pharma;

import jp.chang.myclinic.dto.PharmaDrugDTO;
import jp.chang.myclinic.dto.PharmaDrugNameDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ListAllPharmaDrugDialog extends JDialog {

    private JLabel drugNameLabel;
    private PharmaDrugEditor pharmaDrugEditor;
    private JPanel editorCommandArea;
    private String currentDrugName;
    private PharmaDrugDTO currentPharmaDrug;

    public ListAllPharmaDrugDialog(List<PharmaDrugNameDTO> pharmaDrugNames){
        setTitle("薬剤情報一覧");
        setLayout(new MigLayout("", "[300]", "[grow]"));
        add(makeLeft(pharmaDrugNames));
        add(makeRight(), "top");
        pack();
    }

    private JComponent makeLeft(List<PharmaDrugNameDTO> pharmaDrugNames){
        JPanel panel = new JPanel(new MigLayout("insets 0", "[grow]", "[grow] []"));
        JList<PharmaDrugNameDTO> list = new JList<>();
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setCellRenderer((JList<? extends PharmaDrugNameDTO> aList, PharmaDrugNameDTO value, int index, boolean isSelected, boolean cellHasFocus) -> {
            JLabel label = new JLabel(value.name);
            if( isSelected ){
                label.setBackground(aList.getSelectionBackground());
                label.setForeground(aList.getSelectionForeground());
                label.setOpaque(true);
            }
            return label;
        });
        list.setListData(pharmaDrugNames.toArray(new PharmaDrugNameDTO[]{}));
        JScrollPane sp = new JScrollPane(list);
        panel.add(sp, "h 300");
        list.addListSelectionListener(event -> {
            if( event.getValueIsAdjusting() ){
                return;
            }
            PharmaDrugNameDTO pharmaDrugName = list.getSelectedValue();
            Service.api.getPharmaDrug(pharmaDrugName.iyakuhincode)
                    .thenAccept(pharmaDrug -> {
                        EventQueue.invokeLater(() -> updateRight(pharmaDrug, pharmaDrugName.name));
                    })
                    .exceptionally(t -> {
                        t.printStackTrace();
                        EventQueue.invokeLater(() -> {
                            alert(t.toString());
                        });
                        return null;
                    });
        });
        return panel;
    }

    private JComponent makeRight(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        drugNameLabel = new JLabel();
        pharmaDrugEditor = new PharmaDrugEditor();
        editorCommandArea = new JPanel(new MigLayout("insets 0", "", ""));
        editorCommandArea.add(makeDefaultCommands());
        panel.add(drugNameLabel, "wrap");
        panel.add(pharmaDrugEditor, "wrap");
        panel.add(editorCommandArea);
        return panel;
    }

    private JComponent makeDefaultCommands(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        JButton editButton = new JButton("編集する");
        editButton.addActionListener(event -> {
            pharmaDrugEditor.setEditable(true);
            editorCommandArea.removeAll();
            editorCommandArea.add(makeEditCommands());
            editorCommandArea.repaint();
            editorCommandArea.revalidate();
        });
        panel.add(editButton);
        return panel;
    }

    private JComponent makeEditCommands(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        JButton enterButton = new JButton("入力");
        enterButton.addActionListener(event -> {
            currentPharmaDrug.description = pharmaDrugEditor.getDescription();
            currentPharmaDrug.sideeffect = pharmaDrugEditor.getSideEffect();
            Service.api.updatePharmaDrug(currentPharmaDrug)
                    .thenAccept(result -> {
                        EventQueue.invokeLater(() -> gotoDefault());
                    })
                    .exceptionally(t -> {
                        t.printStackTrace();
                        EventQueue.invokeLater(() -> alert(t.toString()));
                        return null;
                    });
        });
        JButton cancelButton = new JButton("キャンセル");
        cancelButton.addActionListener(event -> {
            gotoDefault();
        });
        panel.add(enterButton);
        panel.add(cancelButton);
        return panel;
    }

    private void gotoDefault(){
        gotoDefault(true);
    }

    private void gotoDefault(boolean repaint){
        drugNameLabel.setText(currentDrugName == null ? "" : currentDrugName);
        pharmaDrugEditor.setEditable(false);
        pharmaDrugEditor.setData(currentPharmaDrug);
        editorCommandArea.removeAll();
        editorCommandArea.add(makeDefaultCommands());
        if( repaint ){
            editorCommandArea.repaint();
            editorCommandArea.revalidate();
        }
    }

    private void updateRight(PharmaDrugDTO pharmaDrug, String name){
        currentDrugName = name;
        currentPharmaDrug = pharmaDrug;
        gotoDefault(false);
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
