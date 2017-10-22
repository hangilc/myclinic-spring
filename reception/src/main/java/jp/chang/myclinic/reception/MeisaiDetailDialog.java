package jp.chang.myclinic.reception;

import jp.chang.myclinic.dto.MeisaiDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.util.NumberUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class MeisaiDetailDialog extends JDialog {

    private JButton closeButton = new JButton("閉じる");

    public MeisaiDetailDialog(Window owner, MeisaiDTO meisai, PatientDTO patient, VisitDTO visit){
        super(owner, "明細の詳細");
        setLayout(new MigLayout("gapy 0", "", ""));
        add(new JLabel(patientLabel(patient)), "wrap");
        add(new JLabel(DateTimeUtil.sqlDateTimeToKanji(visit.visitedAt, DateTimeUtil.kanjiFormatter3, DateTimeUtil.kanjiFormatter4)), "wrap");
        add(new MeisaiDetailPane(meisai), "gaptop 5, wrap");
        add(new JLabel("総点：" + NumberUtil.formatNumber(meisai.totalTen) + "点"), "gaptop 5, wrap");
        add(new JLabel("負担割：" + meisai.futanWari + "割"), "wrap");
        add(new JLabel("請求額：" + NumberUtil.formatNumber(meisai.charge) + "円"), "wrap");
        add(closeButton, "right");
        bind();
        pack();
    }

    private JComponent makeSouth(){
        return closeButton;
    }

    private void bind(){
        closeButton.addActionListener(event -> dispose());
    }

    private String patientLabel(PatientDTO patient){
        return String.format("(%d) %s %S ", patient.patientId, patient.lastName, patient.firstName);
    }

}


