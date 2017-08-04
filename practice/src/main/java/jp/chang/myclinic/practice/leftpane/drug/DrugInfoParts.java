package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.consts.DrugCategory;

import javax.swing.*;

class DrugInfoParts {
    private JLabel nameLabel = new JLabel("");
    private JLabel amountLabel = new JLabel("");
    private JTextField amountField = new JTextField(6);
    private JLabel amountUnit = new JLabel("");
    private JTextField usageField = new JTextField(10);
    private JLabel daysLabel = new JLabel("");
    private JTextField daysField = new JTextField(2);
    private JLabel daysUnit = new JLabel("");
    private JRadioButton naifukuRadio = new JRadioButton("内服");
    private JRadioButton tonpukuRadio = new JRadioButton("屯服");
    private JRadioButton gaiyouRadio = new JRadioButton("外用");
    private ButtonGroup categoryGroup = new ButtonGroup();

    DrugInfoParts(DrugCategory drugCategory){
        categoryGroup.add(naifukuRadio);
        categoryGroup.add(tonpukuRadio);
        categoryGroup.add(gaiyouRadio);
        adaptToCategory(drugCategory);
    }

    private void adaptToCategory(DrugCategory category){
        switch(category){
            case Naifuku: {
                amountLabel.setText("用量");
                daysLabel.setText("日数");
                daysUnit.setText("日分");
                naifukuRadio.setSelected(true);
                break;
            }
            case Tonpuku: {
                amountLabel.setText("一回");
                daysLabel.setText("回数");
                daysUnit.setText("回分");
                tonpukuRadio.setSelected(true);
                break;
            }
            case Gaiyou: {
                amountLabel.setText("用量");
                gaiyouRadio.setSelected(true);
                break;
            }
        }
    }

    public JLabel getNameLabel() {
        return nameLabel;
    }

    public JLabel getAmountLabel() {
        return amountLabel;
    }

    public JTextField getAmountField() {
        return amountField;
    }

    public JLabel getAmountUnit() {
        return amountUnit;
    }

    public JTextField getUsageField() {
        return usageField;
    }

    public JLabel getDaysLabel() {
        return daysLabel;
    }

    public JTextField getDaysField() {
        return daysField;
    }

    public JLabel getDaysUnit() {
        return daysUnit;
    }

    public JRadioButton getNaifukuRadio() {
        return naifukuRadio;
    }

    public JRadioButton getTonpukuRadio() {
        return tonpukuRadio;
    }

    public JRadioButton getGaiyouRadio() {
        return gaiyouRadio;
    }
}
