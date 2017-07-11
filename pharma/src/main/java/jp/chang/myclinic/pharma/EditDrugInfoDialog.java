package jp.chang.myclinic.pharma;

import jp.chang.myclinic.dto.PharmaDrugDTO;
import jp.chang.myclinic.dto.PharmaDrugNameDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class EditDrugInfoDialog extends JDialog {

    private JList<PharmaDrugNameDTO> searchResultList;

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
                                JButton startButton = new JButton("作成");
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
        getContentPane().removeAll();
        setLayout(new MigLayout("", "", ""));
        add(new JLabel(name), "wrap");
        PharmaDrugEditor editor = new PharmaDrugEditor(pharmaDrug);
        add(editor, "wrap");
        JButton enterButton = new JButton("入力");
        enterButton.addActionListener(event -> {
            pharmaDrug.description = editor.getDescription();
            pharmaDrug.sideeffect = editor.getSideEffect();
            Service.api.updatePharmaDrug(pharmaDrug)
                    .thenAccept(result -> {
                        EventQueue.invokeLater(() -> {
                            dispose();
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
        add(enterButton);
        repaint();
        revalidate();
        pack();
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
