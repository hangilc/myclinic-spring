package jp.chang.myclinic.practice.leftpane.shinryou;

import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.dto.ShinryouMasterDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.Link;
import jp.chang.myclinic.practice.Service;
import jp.chang.myclinic.practice.WrappedText;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

class ShinryouEditor extends JPanel {

    interface Callback {
        default void onEnter(ShinryouMasterDTO selectedMaster){}
        default void onDelete(){}
        default void onCancel(){}
    }

    private ShinryouMasterDTO selectedMaster;
    private Callback callback = new Callback(){};

    ShinryouEditor(int width, ShinryouFullDTO shinryouFull, VisitDTO visit){
        setLayout(new MigLayout("insets 0", String.format("[%dpx!]", width), ""));
        JLabel nameLabel = new JLabel("名称：");
        WrappedText nameText = new WrappedText(width - nameLabel.getPreferredSize().width - 4);
        nameText.setText(shinryouFull.master.name);
        this.selectedMaster = shinryouFull.master;
        SearchResult searchResult = new SearchResult();
        searchResult.setCallback(new SearchResult.Callback() {
            @Override
            public void onSelected(ShinryouMasterDTO master) {
                nameText.setText(master.name);
                selectedMaster = master;
            }
        });
        JScrollPane resultScroll = new JScrollPane(searchResult);
        String at = visit.visitedAt.substring(0, 10);
        add(nameLabel, "split 2, gapright 4");
        add(nameText, "wrap");
        add(makeCommandBox(), "growx, wrap");
        add(makeSearchBox(at, searchResult), "growx, wrap");
        add(resultScroll, "growx");
    }

    void setCallback(Callback callback){
        this.callback = callback;
    }

    private Component makeCommandBox(){
        JPanel box = new JPanel(new MigLayout("insets 2", "", ""));
        box.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        JButton enterButton = new JButton("入力");
        enterButton.addActionListener(event -> callback.onEnter(selectedMaster));
        JButton cancelButton = new JButton("キャンセル");
        cancelButton.addActionListener(event -> callback.onCancel());
        Link deleteLink = new Link("削除");
        deleteLink.setCallback(event -> {
            if( !confirm("この診療行為を削除していいですか？") ){
                return;
            }
            callback.onDelete();
        });
        box.add(enterButton);
        box.add(cancelButton);
        box.add(deleteLink);
        return box;
    }

    private Component makeSearchBox(String at, SearchResult searchResult){
        JPanel box = new JPanel(new MigLayout("insets 0", "[grow] []", ""));
        JTextField textField = new JTextField();
        JButton searchButton = new JButton("検索");
        searchButton.addActionListener(event -> {
            String text = textField.getText().trim();
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
        box.add(textField, "growx");
        box.add(searchButton);
        return box;
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

    private boolean confirm(String message){
        int choice = JOptionPane.showConfirmDialog(this, message, "確認", JOptionPane.YES_NO_OPTION);
        return choice == JOptionPane.YES_OPTION;
    }

}
