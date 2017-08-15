package jp.chang.myclinic.practice.newvisitdialog;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class NewVisitDialog extends JDialog {

    public NewVisitDialog() {
        setTitle("診察受付");
        PatientDisp patientDisp = new PatientDisp();
        PatientSearch patientSearch = new PatientSearch();
        JComponent commandBox = makeCommandBox();
        setLayout(new MigLayout("fill", "[grow]", "[] [] [grow]"));
        setPreferredSize(new Dimension(300, 400));
        add(patientDisp, "grow, wrap");
        add(commandBox, "grow, wrap");
        add(patientSearch, "grow");
        pack();
    }

    private JComponent makeCommandBox(){
        JButton enterButton = new JButton("診察受付");
        JPanel panel = new JPanel(new MigLayout("", "", ""));
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        panel.add(enterButton);
        return panel;
    }

}
