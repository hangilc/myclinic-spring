package jp.chang.myclinic.practice.cashierdialog;

import jp.chang.myclinic.dto.MeisaiDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.util.NumberUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class CashierDialog extends JDialog {

    public interface Callback {
        default void onEnter(){}
        default void onCancel(){}
    }

    private Callback callback = new Callback(){};

    public CashierDialog(Window owner, MeisaiDTO meisai, PatientDTO patient, VisitDTO visit){
        super(owner, "会計", ModalityType.DOCUMENT_MODAL);
        setLayout(new MigLayout("gapy 0", "", ""));
        add(new JLabel(patientLabel(patient)), "wrap");
        add(new JLabel(DateTimeUtil.sqlDateTimeToKanji(visit.visitedAt, DateTimeUtil.kanjiFormatter3, DateTimeUtil.kanjiFormatter4)), "wrap");
        add(new MeisaiDetailPane(meisai), "gaptop 5, wrap");
        add(new JLabel("総点：" + NumberUtil.formatNumber(meisai.totalTen) + "点"), "gaptop 5, wrap");
        add(new JLabel("負担割：" + meisai.futanWari + "割"), "wrap");
        add(new JLabel("請求額：" + NumberUtil.formatNumber(meisai.charge) + "円"), "wrap");
        add(makeCommandBox(), "right");
        pack();
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    private Component makeCommandBox(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        JButton enterButton = new JButton("入力");
        enterButton.addActionListener(evt -> callback.onEnter());
        JButton cancelButton = new JButton("キャンセル");
        cancelButton.addActionListener(evt -> callback.onCancel());
        panel.add(enterButton);
        panel.add(cancelButton);
        return panel;
    }

    private String patientLabel(PatientDTO patient){
        return String.format("(%d) %s %S ", patient.patientId, patient.lastName, patient.firstName);
    }


}



