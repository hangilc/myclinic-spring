package jp.chang.myclinic.practice.leftpane;

import jp.chang.myclinic.dto.PatientDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class PatientInfoPane extends JPanel {

    private int paneWidth;
    private PatientDTO patient;
    private JEditorPane infoPane;
    private boolean detailShown = false;

    PatientInfoPane(int width){
        int layoutInsets = 4;
        int colGap = 4;
        setLayout(new MigLayout("insets " + layoutInsets, "[]" + colGap  + "[]", ""));
        JButton detailButton = new JButton("詳細");
        infoPane = new JEditorPane();
        infoPane.setContentType("text/plain");
        infoPane.setText("");
        infoPane.setEditable(false);
        infoPane.setBackground(this.getBackground());
        infoPane.setBorder(BorderFactory.createEmptyBorder());
        this.paneWidth = width - detailButton.getPreferredSize().width - layoutInsets * 2 - colGap;
        detailButton.addActionListener(event -> doToggleDetail());
        add(infoPane, "");
        add(detailButton, "top");
    }

    private String makeText(){
        return String.format("[%d] %s %s (%s %s)", patient.patientId, patient.lastName, patient.firstName,
                patient.lastNameYomi, patient.firstNameYomi);
    }

    private void doToggleDetail(){
        String text = makeText();
        if( !detailShown ){
            text += "\n住所：" + patient.address + "\n電話：" + patient.phone;
        }
        infoPane.setText(text);
        detailShown = !detailShown;
    }

    void setPatient(PatientDTO patient){
        this.patient = patient;
        System.out.println("insets: " + getInsets());
        System.out.println(paneWidth);
        infoPane.setSize(paneWidth, Integer.MAX_VALUE);
        infoPane.setText(makeText());
        repaint();
        revalidate();
    }

    void reset(){
        this.patient = null;
        infoPane.setText("");
        repaint();
        revalidate();
    }
}
