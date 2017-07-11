package jp.chang.myclinic.pharma;

import jp.chang.myclinic.dto.IyakuhinMasterDTO;
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
                                    IyakuhinMasterDTO selectedMaster = searchResultList.getSelectedValue();
                                    if( selectedMaster != null ){
                                        doTransitToEdit(selectedMaster);
                                    }
                                });
                                add(startButton);
                            } else {
                                searchResultList.setListData(masters.toArray(new IyakuhinMasterDTO[]{}));
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

    private JList<PharmaDrugNameDTO> makeSearchResultList(List<PharmaDrugNameDTO> masters){
        JList<PharmaDrugNameDTO> result = new JList<>();
        result.setCellRenderer((list, master, index, isSelected, cellHasFocus) -> {
            JLabel comp = new JLabel(master.name);
            if( isSelected ){
                comp.setBackground(list.getSelectionBackground());
                comp.setForeground(list.getSelectionForeground());
                comp.setOpaque(true);
            }
            return comp;
        });
        result.setListData(masters.toArray(new IyakuhinMasterDTO[]{}));
        return result;
    }

    private void doTransitToEdit(PharmaDrugNameDTO data){
        Service.api.findPharmaDrug(master.iyakuhincode)
                .thenAccept(pharma -> {
                    if( pharma == null ){
                        doOpenEditor(master);
                    } else {
                        EventQueue.invokeLater(() -> {
                            String msg = master.name + "はすでに登録されています。\n内容を表示しますか？";
                            int choice = JOptionPane.showConfirmDialog(this, msg, "オプションの選択",
                                    JOptionPane.OK_CANCEL_OPTION);
                            if( choice == JOptionPane.OK_OPTION ){
                                // TODO: open pharma drug editor
                            }
                        });
                    }
                })
                .exceptionally(t -> {
                    t.printStackTrace();
                    EventQueue.invokeLater(() -> {
                        alert(t.toString());
                    });
                    return null;
                });
    }

    private void doOpenEditor(IyakuhinMasterDTO master){
        getContentPane().removeAll();
        setLayout(new MigLayout("", "", ""));
        add(new JLabel(master.name), "wrap");
        PharmaDrugEditor editor = new PharmaDrugEditor();
        add(editor, "wrap");
        JButton enterButton = new JButton("入力");
        enterButton.addActionListener(event -> {
            PharmaDrugDTO pharmaDrug = new PharmaDrugDTO();
            pharmaDrug.iyakuhincode = master.iyakuhincode;
            pharmaDrug.description = editor.getDescription();
            pharmaDrug.sideeffect = editor.getSideEffect();
            System.out.println("pharmaDrugDTO (2): " + pharmaDrug);
            Service.api.enterPharmaDrug(pharmaDrug)
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
