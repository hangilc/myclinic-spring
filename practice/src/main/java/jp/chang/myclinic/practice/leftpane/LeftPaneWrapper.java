package jp.chang.myclinic.practice.leftpane;

import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitFull2PageDTO;
import jp.chang.myclinic.practice.MainExecContext;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class LeftPaneWrapper extends JPanel {

    private MainExecContext mainExecContext;
    private PatientInfoPane patientInfoPane;
    private RecordsNav topNav;
    private DispRecords dispRecords;
    private RecordsNav bottomNav;

    public LeftPaneWrapper(MainExecContext mainExecContext){
        setLayout(new MigLayout("insets 0", "[grow]", "[] [] [grow] []"));
        this.mainExecContext = mainExecContext;
        EventQueue.invokeLater(() -> setupComponents());
    }

    private void setupComponents(){
        int width = getSize().width;
        this.patientInfoPane = new PatientInfoPane(width);
        this.topNav = new RecordsNav();
        this.bottomNav = new RecordsNav();
        dispRecords = new DispRecords(width, mainExecContext);
        JScrollPane dispScroll = new JScrollPane(dispRecords);
        dispScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        add(this.patientInfoPane, "wrap");
        add(topNav, "wrap");
        add(dispScroll, "grow, wrap");
        add(bottomNav, "");
        revalidate();
    }

    public void reset(){
        setVisible(false);
        repaint();
        revalidate();
    }

    public void start(VisitFull2PageDTO page){
        PatientDTO patient = mainExecContext.getCurrentPatient();
        patientInfoPane.setPatient(patient);
        dispRecords.setVisits(page.visits);
        repaint();
        revalidate();
        System.out.println(page);
    }
}
