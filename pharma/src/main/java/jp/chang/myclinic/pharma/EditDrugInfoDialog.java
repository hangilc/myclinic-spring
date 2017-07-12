package jp.chang.myclinic.pharma;

import jp.chang.myclinic.dto.PharmaDrugDTO;
import jp.chang.myclinic.dto.PharmaDrugNameDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class EditDrugInfoDialog extends JDialog {

    private JList<PharmaDrugNameDTO> searchResultList;
    private String currentDrugName;
    private PharmaDrugDTO pharmaDrug;
    private PharmaDrugEditor editor;
    private JPanel commandArea;

    public EditDrugInfoDialog(){
        setTitle("薬剤情報の表示・編集");
        setLayout(new MigLayout("", "", ""));
        add(new JLabel("薬剤検索"), "wrap");
        JTextField searchTextField = new JTextField(12);
        add(searchTextField);
        JButton searchButton = new JButton("検索");
        add(searchButton);
        searchButton.addActionListener(event -> {
            String searchText = searchTextField.getText();
            if( searchText.isEmpty() ){
                return;
            }
            Service.api.searchPharmaDrugNames(searchText)
                    .thenAccept(result -> {
                        EventQueue.invokeLater(() -> {
                            if( searchResultList == null ){
                                searchResultList = makeSearchResultList(result);
                                JScrollPane sp = new JScrollPane(searchResultList);
                                add(sp, "newline, span, growx, w n:n:300, h n:n:360, wrap");
                                JButton startButton = new JButton("表示");
                                startButton.addActionListener(ev -> {
                                    PharmaDrugNameDTO selectedData = searchResultList.getSelectedValue();
                                    if( selectedData != null ){
                                        doTransitToEdit(selectedData.iyakuhincode, selectedData.name);
                                    }
                                });
                                add(startButton);
                            } else {
                                searchResultList.setListData(result.toArray(new PharmaDrugNameDTO[]{}));
                            }
                            searchResultList.repaint();
                            searchResultList.revalidate();
                            pack();
                        });
                    })
                    .exceptionally(t -> {
                        t.printStackTrace();
                        EventQueue.invokeLater(() -> {
                            alert(t.toString());
                        });
                        return null;
                    });
        });
        pack();
    }

    private JList<PharmaDrugNameDTO> makeSearchResultList(List<PharmaDrugNameDTO> dataList){
        JList<PharmaDrugNameDTO> result = new JList<>();
        result.setCellRenderer((list, data, index, isSelected, cellHasFocus) -> {
            JLabel comp = new JLabel(data.name);
            if( isSelected ){
                comp.setBackground(list.getSelectionBackground());
                comp.setForeground(list.getSelectionForeground());
                comp.setOpaque(true);
            }
            return comp;
        });
        result.setListData(dataList.toArray(new PharmaDrugNameDTO[]{}));
        return result;
    }

    private void doTransitToEdit(int iyakuhincode, String name) {
        Service.api.getPharmaDrug(iyakuhincode)
                .thenAccept(pharma -> {
                    doOpenEditor(pharma, name);
                })
                .exceptionally(t -> {
                    t.printStackTrace();
                    EventQueue.invokeLater(() -> {
                        alert(t.toString());
                    });
                    return null;
                });
    }

    private void doOpenEditor(PharmaDrugDTO pharmaDrug, String name){
        this.currentDrugName = name;
        this.pharmaDrug = pharmaDrug;
        getContentPane().removeAll();
        setLayout(new MigLayout("", "", ""));
        add(new JLabel(name), "wrap");
        this.editor = new PharmaDrugEditor(pharmaDrug);
        editor.setEditable(false);
        add(editor, "wrap");
        this.commandArea = new JPanel(new MigLayout("insets 0", "", ""));
        setupDispModeCommands(false);
        add(commandArea);
        repaint();
        revalidate();
        pack();
    }

    private JButton makeEditButton(){
        JButton editButton = new JButton("編集");
        editButton.addActionListener(event -> {
            editor.setEditable(true);
            setupEditModeCommands();
        });
        return editButton;
    }

    private JButton makeEnterButton(){
        JButton enterButton = new JButton("入力");
        enterButton.addActionListener(evt -> {
            pharmaDrug.description = editor.getDescription();
            pharmaDrug.sideeffect = editor.getSideEffect();
            Service.api.updatePharmaDrug(pharmaDrug)
                    .thenAccept(result -> {
                        EventQueue.invokeLater(this::dispose);
                    })
                    .exceptionally(t -> {
                        t.printStackTrace();
                        EventQueue.invokeLater(() -> {
                            alert(t.toString());
                        });
                        return null;
                    });
        });
        return enterButton;
    }

    private JButton makeDeleteButton(){
        JButton deleteButton = new JButton("削除");
        deleteButton.addActionListener(event -> {
            int reply = JOptionPane.showConfirmDialog(this, currentDrugName + "の薬剤情報を削除しますか？",
                    "確認", JOptionPane.OK_CANCEL_OPTION);
            if (reply == JOptionPane.OK_OPTION) {
                Service.api.deletePharmaDrug(pharmaDrug.iyakuhincode)
                        .thenAccept(result -> {
                            dispose();
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
        return deleteButton;
    }

    private JButton makeCancelButton(){
        JButton button = new JButton("キャンセル");
        button.addActionListener(event -> {
            editor.setEditable(false);
            editor.setData(pharmaDrug);
            setupDispModeCommands(true);
        });
        return button;
    }

    private void setupDispModeCommands(boolean repaint){
        if( repaint ){
            commandArea.removeAll();
        }
        commandArea.add(makeEditButton());
        if( repaint ){
            commandArea.repaint();
            commandArea.revalidate();
        }
    }

    private void setupEditModeCommands(){
        commandArea.removeAll();
        commandArea.add(makeEnterButton());
        commandArea.add(makeDeleteButton());
        commandArea.add(makeCancelButton());
        commandArea.repaint();
        commandArea.revalidate();
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
