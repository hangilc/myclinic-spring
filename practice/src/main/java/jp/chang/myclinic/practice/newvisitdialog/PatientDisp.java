package jp.chang.myclinic.practice.newvisitdialog;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class PatientDisp extends JPanel {

    private JLabel patientIdLabel = new JLabel("");
    private JEditorPane nameLabel = new JEditorPane("text/plain", "");
    private JEditorPane yomiLabel = new JEditorPane("text/plain", "");
    private JLabel birthdayLabel = new JLabel("");
    private JLabel sexLabel = new JLabel("");
    private JEditorPane addressLabel = new JEditorPane("text/plain", "");
    private JEditorPane phoneLabel = new JEditorPane("text/plain", "");

    PatientDisp(){
        setLayout(new MigLayout("insets 0", "[] [grow]", ""));
        nameLabel.setEditable(false);
        nameLabel.setBackground(getBackground());
        yomiLabel.setEditable(false);
        yomiLabel.setBackground(getBackground());
        addressLabel.setEditable(false);
        addressLabel.setBackground(getBackground());
        phoneLabel.setEditable(false);
        phoneLabel.setBackground(getBackground());
        add(new JLabel("患者番号"));
        add(patientIdLabel, "wrap");
        add(new JLabel("名前"));
        add(nameLabel, "wrap");
        add(new JLabel("よみ"));
        add(yomiLabel, "wrap");
        add(new JLabel("生年月日"));
        add(birthdayLabel, "wrap");
        add(new JLabel("性別"));
        add(sexLabel, "wrap");
        add(new JLabel("住所"));
        add(addressLabel, "wrap");
        add(new JLabel("電話"));
        add(phoneLabel, "wrap");
    }
}
