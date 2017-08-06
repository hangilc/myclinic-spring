package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import jp.chang.myclinic.practice.Link;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class DrugInfoBase extends JPanel {

    int iyakuhincode;
    JLabel drugNameLabel = new JLabel();
    JLabel amountLabel = new JLabel("");
    JTextField amountField = new JTextField(6);
    JLabel amountUnit = new JLabel("");
    JTextField usageField = new JTextField(10);
    Link usageExampleLink = new Link("例");
    JPanel usageExampleWrapper;
    JLabel daysLabel = new JLabel("");
    JTextField daysField = new JTextField(2);
    JLabel daysUnit = new JLabel("");
    private JRadioButton naifukuRadio = new JRadioButton("内服");
    private JRadioButton tonpukuRadio = new JRadioButton("屯服");
    private JRadioButton gaiyouRadio = new JRadioButton("外用");
    JPanel categoryPane;

    DrugInfoBase(){
        setLayout(new MigLayout("insets 0", "", ""));
        usageExampleWrapper = createUsageExampleWrapper();
        ButtonGroup categoryGroup = new ButtonGroup();
        categoryGroup.add(naifukuRadio);
        categoryGroup.add(tonpukuRadio);
        categoryGroup.add(gaiyouRadio);
        categoryPane = new JPanel(new MigLayout("insets 0", "", ""));
        categoryPane.add(naifukuRadio);
        categoryPane.add(tonpukuRadio);
        categoryPane.add(gaiyouRadio);
        naifukuRadio.addActionListener(event -> adaptToCategory(DrugCategory.Naifuku));
        tonpukuRadio.addActionListener(event -> adaptToCategory(DrugCategory.Tonpuku));
        gaiyouRadio.addActionListener(event -> adaptToCategory(DrugCategory.Gaiyou));
        usageExampleWrapper.setVisible(false);
        usageExampleLink.setCallback(event -> {
            usageExampleWrapper.setVisible(!usageExampleWrapper.isVisible());
        });
    }

    private JPanel createUsageExampleWrapper(){
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

    void setDaysVisible(boolean visible){
        daysLabel.setVisible(visible);
        daysField.setVisible(visible);
        daysUnit.setVisible(visible);
    }

    void setCategory(DrugCategory category){
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

    void setMaster(IyakuhinMasterDTO master){
        this.iyakuhincode = master.iyakuhincode;
        drugNameLabel.setText(master.name);
        amountUnit.setText(master.unit);
    }


}
