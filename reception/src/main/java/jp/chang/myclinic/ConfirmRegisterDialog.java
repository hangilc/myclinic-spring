package jp.chang.myclinic;

import jp.chang.myclinic.dto.PatientDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Created by hangil on 2017/06/04.
 */
public class ConfirmRegisterDialog extends JDialog {

    private boolean canceled = true;
    private JButton okButton = new JButton("はい");
    private JButton cancelButton = new JButton("キャンセル");

    public ConfirmRegisterDialog(Window owner, PatientDTO patient){
        super(owner, "診療受付の確認", ModalityType.DOCUMENT_MODAL);
        setLayout(new MigLayout("", "", ""));
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.setPatient(patient);
        JScrollPane sp = new JScrollPane(patientInfo);
        sp.setBorder(BorderFactory.createEmptyBorder());
        add(sp, "w 280, wrap");
        add(new JLabel("この患者の診察を受付ますか？"), "wrap");
        add(makeSouth(), "right");
        bind();
        pack();
    }

    public boolean isCanceled(){
        return canceled;
    }

    private JComponent makeSouth(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        panel.add(okButton, "sizegroup btn");
        panel.add(cancelButton, "sizegroup btn");
        return panel;
    }

    private void bind(){
        okButton.addActionListener(event -> {
            canceled = false;
            dispose();
        });
        cancelButton.addActionListener(event -> {
            canceled = true;
            dispose();
        });
    }
}
