package jp.chang.myclinic.pharma;

import jp.chang.myclinic.dto.PatientDTO;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AuxDispVisitsNav extends JPanel {

    private List<RecordPage> pages;
    private JLabel currentPageLabel;
    private JLabel gotoPrevLink;
    private JLabel gotoNextLink;

    public AuxDispVisitsNav(List<RecordPage> pages, PatientDTO patient){
        this.pages = pages;
        setLayout(new FlowLayout());
        if( pages.size() > 1 ){
            currentPageLabel = new JLabel("1");
            add(currentPageLabel);
            add(new JLabel("/"));
            add(new JLabel("" + pages.size()));
            gotoPrevLink = new JLabel("<");
            gotoNextLink = new JLabel(">");
            add(gotoPrevLink);
            add(gotoNextLink);
        }
        add(new JLabel("(" + patient.lastName + patient.firstName + ")"));
    }
}
