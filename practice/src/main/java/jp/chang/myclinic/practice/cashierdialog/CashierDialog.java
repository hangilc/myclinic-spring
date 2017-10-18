package jp.chang.myclinic.practice.cashierdialog;

import jp.chang.myclinic.dto.MeisaiDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.Link;
import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.util.NumberUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class CashierDialog extends JDialog {

    public interface Callback {
        default void onEnter(int charge){}
        default void onCancel(){}
    }

    private int charge;
    private Component modifyWorkArea;
    private JLabel chargeLabel = new JLabel("");
    private JTextField chargeField = new JTextField(6);
    private Callback callback = new Callback(){};

    public CashierDialog(Window owner, MeisaiDTO meisai, PatientDTO patient, VisitDTO visit){
        super(owner, "会計", ModalityType.DOCUMENT_MODAL);
        this.charge = meisai.charge;
        setLayout(new MigLayout("gapy 0, hidemode 3", "", ""));
        Link modifyLink = new Link("変更");
        modifyLink.setCallback(evt -> {
            chargeField.setText("" + charge);
            modifyWorkArea.setVisible(true);
            revalidate();
            repaint();
            pack();
        });
        modifyWorkArea = makeModifyWorkArea();
        updateChargeLabel();
        add(new JLabel(patientLabel(patient)), "wrap");
        add(new JLabel(DateTimeUtil.sqlDateTimeToKanji(visit.visitedAt, DateTimeUtil.kanjiFormatter3, DateTimeUtil.kanjiFormatter4)), "wrap");
        add(new MeisaiDetailPane(meisai), "gaptop 5, wrap");
        add(new JLabel("総点：" + NumberUtil.formatNumber(meisai.totalTen) + "点"), "gaptop 5, wrap");
        add(new JLabel("負担割：" + meisai.futanWari + "割"), "wrap");
        add(chargeLabel, "split 2");
        add(modifyLink, "wrap");
        add(modifyWorkArea, "wrap");
        add(makeCommandBox(), "right");
        pack();
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    private void updateChargeLabel(){
        chargeLabel.setText("請求額：" + NumberUtil.formatNumber(charge) + "円");
    }

    private Component makeModifyWorkArea(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        JButton enterButton = new JButton("入力");
        enterButton.addActionListener(evt -> {
            try {
                this.charge = Integer.parseInt(chargeField.getText());
                updateChargeLabel();
                panel.setVisible(false);
                revalidate();
                repaint();
                pack();
            } catch(NumberFormatException ex){
                alert("請求額の入力が不適切です。");
            }
        });
        JButton cancelButton = new JButton("キャンセル");
        cancelButton.addActionListener(evt -> {
            panel.setVisible(false);
            revalidate();
            repaint();
            pack();
        });
        panel.add(chargeField);
        panel.add(new JLabel("円"));
        panel.add(enterButton);
        panel.add(cancelButton);
        panel.setVisible(false);
        return panel;
    }

    private Component makeCommandBox(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        JButton enterButton = new JButton("入力");
        enterButton.addActionListener(evt -> callback.onEnter(charge));
        JButton cancelButton = new JButton("キャンセル");
        cancelButton.addActionListener(evt -> callback.onCancel());
        panel.add(enterButton);
        panel.add(cancelButton);
        return panel;
    }

    private String patientLabel(PatientDTO patient){
        return String.format("(%d) %s %S ", patient.patientId, patient.lastName, patient.firstName);
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}



