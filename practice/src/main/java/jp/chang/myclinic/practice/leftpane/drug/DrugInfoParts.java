package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.practice.Link;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class DrugInfoParts {

    interface Callback {
        default void onSetDaysVisible(boolean visible){};
    }

    private JLabel nameLabel = new JLabel("");
    private JLabel amountLabel = new JLabel("");
    private JTextField amountField = new JTextField(6);
    private JLabel amountUnit = new JLabel("");
    private JTextField usageField = new JTextField(10);
    private Link exampleUsageLink = new Link("例");
    private JPanel exampleWrapper;
    private JLabel daysLabel = new JLabel("");
    private JTextField daysField = new JTextField(2);
    private JLabel daysUnit = new JLabel("");
    private JRadioButton naifukuRadio = new JRadioButton("内服");
    private JRadioButton tonpukuRadio = new JRadioButton("屯服");
    private JRadioButton gaiyouRadio = new JRadioButton("外用");
    private JPanel categoryPane;
    private Callback callback = new Callback(){};

    DrugInfoParts(){
        ButtonGroup categoryGroup = new ButtonGroup();
        categoryGroup.add(naifukuRadio);
        categoryGroup.add(tonpukuRadio);
        categoryGroup.add(gaiyouRadio);
        categoryPane = new JPanel(new MigLayout("insets 0", "", ""));
        categoryPane.add(naifukuRadio);
        categoryPane.add(tonpukuRadio);
        categoryPane.add(gaiyouRadio);
        naifukuRadio.addActionListener(event -> {
            setCategory(DrugCategory.Naifuku);
        });
        tonpukuRadio.addActionListener(event -> {
            setCategory(DrugCategory.Tonpuku);
        });
        gaiyouRadio.addActionListener(event -> {
            setCategory(DrugCategory.Gaiyou);
        });
        setCategory(DrugCategory.Naifuku);
        exampleWrapper = createExampleWrapper();
        exampleWrapper.setVisible(false);
        exampleUsageLink.setCallback(event -> {
            exampleWrapper.setVisible(!exampleWrapper.isVisible());
        });
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    private void adaptToCategory(DrugCategory category){
        switch(category){
            case Naifuku: {
                amountLabel.setText("用量");
                daysLabel.setText("日数");
                daysUnit.setText("日分");
                setDaysVisible(true);
                break;
            }
            case Tonpuku: {
                amountLabel.setText("一回");
                daysLabel.setText("回数");
                daysUnit.setText("回分");
                setDaysVisible(true);
                break;
            }
            case Gaiyou: {
                amountLabel.setText("用量");
                setDaysVisible(false);
                break;
            }
        }
    }

    private void setDaysVisible(boolean visible){
        daysLabel.setVisible(visible);
        daysField.setVisible(visible);
        daysUnit.setVisible(visible);
        callback.onSetDaysVisible(visible);
    }

    private void setCategory(DrugCategory category){
        switch(category){
            case Naifuku: {
                naifukuRadio.setSelected(true);
                adaptToCategory(DrugCategory.Naifuku);
                break;
            }
            case Tonpuku: {
                tonpukuRadio.setSelected(true);
                adaptToCategory(DrugCategory.Tonpuku);
                break;
            }
            case Gaiyou: {
                gaiyouRadio.setSelected(true);
                adaptToCategory(DrugCategory.Gaiyou);
                break;
            }
        }
    }

    private JPanel createExampleWrapper(){
        JPanel wrapper = new JPanel(new MigLayout("", ", "));
        String[] examples = new String[]{"分１　朝食後", "分２　朝夕食後", "分３　毎食後", "分１　寝る前"};
        for(int i=0;i<examples.length;i++){
            String text = examples[i];
            wrapper.add(new Link(text, event -> {
                usageField.setText(text);
                wrapper.setVisible(false);
            }), i == (examples.length-1) ? "" : "wrap");
        }
        return wrapper;
    }

    JLabel getNameLabel() {
        return nameLabel;
    }

    JLabel getAmountLabel() {
        return amountLabel;
    }

    JTextField getAmountField() {
        return amountField;
    }

    JLabel getAmountUnit() {
        return amountUnit;
    }

    JTextField getUsageField() {
        return usageField;
    }

    JLabel getDaysLabel() {
        return daysLabel;
    }

    JTextField getDaysField() {
        return daysField;
    }

    JLabel getDaysUnit() {
        return daysUnit;
    }

    JComponent getCatgegoryPane(){
        return categoryPane;
    }

    Link getUsageExampleLink(){
        return exampleUsageLink;
    }

    JPanel getUsageExampleWrapper(){
        return exampleWrapper;
    }
}
