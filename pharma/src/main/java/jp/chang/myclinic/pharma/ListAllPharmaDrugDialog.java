package jp.chang.myclinic.pharma;

import jp.chang.myclinic.dto.PharmaDrugDTO;
import jp.chang.myclinic.dto.PharmaDrugNameDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ListAllPharmaDrugDialog extends JDialog {

    private JList<PharmaDrugNameDTO> pharmaDrugList;
    private JLabel drugNameLabel;
    private PharmaDrugEditor pharmaDrugEditor;
    private JPanel editorCommandArea;
    private String currentDrugName;
    private PharmaDrugDTO currentPharmaDrug;
    private JMenuItem deleteMenuItem;

    public ListAllPharmaDrugDialog(List<PharmaDrugNameDTO> pharmaDrugNames){
        setTitle("薬剤情報一覧");
        setupMenu();
        setLayout(new MigLayout("", "[300]", "[grow]"));
        add(makeLeft(pharmaDrugNames));
        add(makeRight(), "top");
        pack();
    }

    private void setupMenu(){
        JMenuBar menuBar = new JMenuBar();
        JMenu manipMenu = new JMenu("その他の操作");
        deleteMenuItem = new JMenuItem("薬剤情報を削除");
        deleteMenuItem.addActionListener(event -> {
            int reply = JOptionPane.showConfirmDialog(this, currentDrugName + "の薬剤情報を削除しますか？", "確認", JOptionPane.OK_CANCEL_OPTION);
            if (reply == JOptionPane.OK_OPTION) {
                Service.api.deletePharmaDrug(currentPharmaDrug.iyakuhincode)
                        .thenAccept(result -> {
                            EventQueue.invokeLater(() -> {
                                reloadDrugList();
                                gotoBlank();
                            });
                        })
                        .exceptionally(t -> {
                            t.printStackTrace();
                            EventQueue.invokeLater(() -> {
                                alert(t.toString());
                            });
                            return null;
                        });
            }
        });
        deleteMenuItem.setEnabled(false);
        manipMenu.add(deleteMenuItem);
        menuBar.add(manipMenu);
        setJMenuBar(menuBar);
    }

    private JComponent makeLeft(List<PharmaDrugNameDTO> pharmaDrugNames){
        JPanel panel = new JPanel(new MigLayout("insets 0", "[grow]", "[grow] []"));
        JList<PharmaDrugNameDTO> list = new JList<>();
        pharmaDrugList = list;
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
            if( pharmaDrugName == null ){
                return;
            }
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
        panel.add(drugNameLabel, "wrap");
        panel.add(pharmaDrugEditor, "wrap");
        panel.add(editorCommandArea);
        return panel;
    }

    private JComponent makeDefaultCommands(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        JButton editButton = new JButton("編集");
        editButton.addActionListener(event -> {
            pharmaDrugEditor.setEditable(true);
            editorCommandArea.removeAll();
            editorCommandArea.add(makeEditCommands());
            editorCommandArea.repaint();
            editorCommandArea.revalidate();
        });
        JButton copyButton = new JButton("新規作成");
        copyButton.addActionListener(event -> {
            int reply = JOptionPane.showConfirmDialog(this,
                    "この薬剤情報をもとに別の薬剤情報を作成しますか？", "確認", JOptionPane.OK_CANCEL_OPTION);
            if( reply == JOptionPane.OK_OPTION ){
                NewDrugInfoDialog dialog = new NewDrugInfoDialog(currentPharmaDrug){
                    @Override
                    public void onPharmaDrugEntered(PharmaDrugDTO newPharmaDrug){
                        reloadDrugList();
                    }
                };
                dialog.setLocationByPlatform(true);
                dialog.setVisible(true);
            }
        });
        panel.add(editButton);
        panel.add(copyButton);
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

    private void gotoBlank() {
        currentDrugName = null;
        currentPharmaDrug = null;
        drugNameLabel.setText("");
        pharmaDrugEditor.setEditable(false);
        pharmaDrugEditor.clear();
        editorCommandArea.removeAll();
        editorCommandArea.repaint();
        editorCommandArea.revalidate();
    }

    private void updateRight(PharmaDrugDTO pharmaDrug, String name){
        currentDrugName = name;
        currentPharmaDrug = pharmaDrug;
        deleteMenuItem.setEnabled(true);
        gotoDefault(false);
    }

    private void reloadDrugList(){
        Service.api.listAllPharmaDrugNames()
                .thenAccept(result -> {
                    EventQueue.invokeLater(() -> pharmaDrugList.setListData(result.toArray(new PharmaDrugNameDTO[]{})));
                })
                .exceptionally(t -> {
                    t.printStackTrace();
                    EventQueue.invokeLater(() -> alert(t.toString()));
                    return null;
                });
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
