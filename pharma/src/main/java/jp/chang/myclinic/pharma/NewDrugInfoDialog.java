package jp.chang.myclinic.pharma;

import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class NewDrugInfoDialog extends JDialog {

    private JList<IyakuhinMasterDTO> searchResultList;

    NewDrugInfoDialog(){
        setTitle("新規薬剤情報入力");
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
            Service.api.searchIyakuhinMasterByName(searchText, LocalDate.now().toString())
                    .thenAccept(masters -> {
                        EventQueue.invokeLater(() -> {
                            if( searchResultList == null ){
                                searchResultList = makeSearchResultList(masters);
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

    private JList<IyakuhinMasterDTO> makeSearchResultList(List<IyakuhinMasterDTO> masters){
        JList<IyakuhinMasterDTO> result = new JList<>();
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

    private void doTransitToEdit(IyakuhinMasterDTO master){
        removeAll();
        repaint();
        revalidate();
        pack();
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
