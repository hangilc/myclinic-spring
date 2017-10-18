package jp.chang.myclinic.practice.leftpane.conduct;

import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import jp.chang.myclinic.practice.Service;
import jp.chang.myclinic.practice.WrappedText;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

class EnterInjectForm extends JPanel {

    interface Callback {
        default void onEnter(IyakuhinMasterDTO master, double amount, String shinryouName){}
        default void onCancel(){}
    }

    private IyakuhinMasterDTO selectedMaster;
    private JTextField amountField;
    private JRadioButton hikaRadio;
    private JRadioButton jyouchuuRadio;
    private Callback callback = new Callback(){};

    EnterInjectForm(int width, String at){
        setLayout(new MigLayout("insets 0", String.format("[%dpx!]", width), ""));
        JLabel nameLabel = new JLabel("名称：");
        WrappedText nameText = new WrappedText(width - 4 - nameLabel.getPreferredSize().width);
        nameText.setText("");
        JLabel amountLabel = new JLabel("用量：");
        amountField = new JTextField(4);
        JLabel amountUnit = new JLabel("");
        hikaRadio = new JRadioButton("皮下筋注");
        jyouchuuRadio = new JRadioButton("静注");
        ButtonGroup group = new ButtonGroup();
        group.add(hikaRadio);
        group.add(jyouchuuRadio);
        hikaRadio.setSelected(true);
        DrugSearchResult searchResult = new DrugSearchResult();
        searchResult.setCallback(new DrugSearchResult.Callback() {
            @Override
            public void onSelected(IyakuhinMasterDTO master) {
                selectedMaster = master;
                nameText.setText(master.name);
                amountUnit.setText(master.unit);
            }
        });
        JScrollPane resultScroll = new JScrollPane(searchResult);
        add(nameLabel, "split 2, gapright 4");
        add(nameText, "gapleft 4, wrap");
        add(amountLabel, "split 3, gapright 4");
        add(amountField, "gapleft 4");
        add(amountUnit, "wrap");
        add(hikaRadio, "split 2");
        add(jyouchuuRadio, "wrap");
        add(makeCommandBox(), "span, growx, wrap");
        add(makeSearchBox(at, searchResult), "span, growx, wrap");
        add(resultScroll, "span, growx");
    }

    void setCallback(Callback callback){
        this.callback = callback;
    }

    private String getShinryouName(){
        if( hikaRadio.isSelected() ){
            return hikaRadio.getText();
        } else if( jyouchuuRadio.isSelected() ){
            return jyouchuuRadio.getText();
        } else {
            throw new RuntimeException("cannot find inject kind");
        }
    }

    private Component makeCommandBox(){
        JPanel box = new JPanel(new MigLayout("insets 2", "", ""));
        box.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        JButton enterButton = new JButton("入力");
        enterButton.addActionListener(event -> {
            if( selectedMaster == null ){
                alert("医薬品が指定されていません。");
                return;
            }
            double amount;
            try {
                amount = Double.parseDouble(amountField.getText());
            } catch(NumberFormatException ex){
                alert("用量の入力が不適切です。");
                return;
            }
            callback.onEnter(selectedMaster, amount, getShinryouName());
        });
        JButton cancelButton = new JButton("キャンセル");
        cancelButton.addActionListener(event -> callback.onCancel());
        box.add(enterButton);
        box.add(cancelButton);
        return box;
    }

    private Component makeSearchBox(String at, DrugSearchResult searchResult){
        JPanel box = new JPanel(new MigLayout("insets 0", "[grow] []", ""));
        JTextField textField = new JTextField();
        JButton searchButton = new JButton("検索");
        searchButton.addActionListener(event -> {
            String text = textField.getText().trim();
            if( text.isEmpty() ){
                return;
            }
            Service.api.searchIyakuhinMaster(text, at)
                    .thenAccept(masters -> EventQueue.invokeLater(() ->{
                        searchResult.setListData(masters.toArray(new IyakuhinMasterDTO[]{}));
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

}
