package jp.chang.myclinic.practice.leftpane.shinryou;

import jp.chang.myclinic.dto.ShinryouMasterDTO;
import jp.chang.myclinic.practice.Service;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

class SearchAndAddPane extends JPanel {

    interface Callback {
        default void onCancel(){}
    }

    //private int width;
    String at;
    JTextField searchField;
    JButton searchButton;
    SearchResult searchResult;

    private Callback callback = new Callback(){};

    SearchAndAddPane(int width, String at){
        //this.width = width;
        this.at = at;
        setLayout(new MigLayout("insets 0", String.format("[%dpx!]", width), ""));
        searchField = new JTextField(4);
        searchButton = new JButton("検索");
        searchResult = new SearchResult();
        JScrollPane searchScroll = new JScrollPane(searchResult);
        setupSearch();
        add(makeCommandBox(), "growx, wrap");
        add(searchField, "split 2, growx");
        add(searchButton, "wrap");
        add(searchScroll, "growx");
    }

    void setCallback(Callback callback){
        this.callback = callback;
    }

    private Component makeCommandBox(){
        JPanel panel = new JPanel(new MigLayout("insets 2", "", ""));
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        JButton enterButton = new JButton("入力");
        JButton cancelButton = new JButton("キャンセル");
        cancelButton.addActionListener(event -> callback.onCancel());
        panel.add(enterButton);
        panel.add(cancelButton);
        return panel;
    }

    private void setupSearch(){
        searchResult.setCellRenderer((list, result, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel();
            label.setText(result.name);
            if( isSelected ){
                label.setBackground(list.getSelectionBackground());
                label.setForeground(list.getSelectionForeground());
            } else {
                label.setBackground(list.getBackground());
                label.setForeground(list.getForeground());
            }
            label.setOpaque(true);
            return label;
        });
        searchResult.addListSelectionListener(event -> {
            if( !event.getValueIsAdjusting() ){
                ShinryouMasterDTO master = searchResult.getSelectedValue();
                if( master != null ){
                    System.out.println(master.name);
                }
            }
        });
        searchButton.addActionListener(event -> {
            String text = searchField.getText();
            if( text.isEmpty() ){
                return;
            }
            Service.api.searchShinryouMaster(text, at)
                    .thenAccept(masters -> EventQueue.invokeLater(() ->{
                        searchResult.setListData(masters.toArray(new ShinryouMasterDTO[]{}));
                    }))
                    .exceptionally(t -> {
                        t.printStackTrace();
                        EventQueue.invokeLater(() -> {
                            alert(t.toString());
                        });
                        return null;
                    });

        });
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
