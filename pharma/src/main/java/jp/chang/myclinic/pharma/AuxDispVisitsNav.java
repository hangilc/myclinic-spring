package jp.chang.myclinic.pharma;

import jp.chang.myclinic.dto.PatientDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class AuxDispVisitsNav extends JPanel {

    private AuxDispVisitsRecords records;
    private int currentPage;
    private List<RecordPage> pages;
    private JLabel currentPageLabel;
    private JLabel gotoPrevLink;
    private JLabel gotoNextLink;

    public AuxDispVisitsNav(AuxDispVisitsRecords records, List<RecordPage> pages, PatientDTO patient){
        this.records = records;
        this.pages = pages;
        setLayout(new FlowLayout());
        if( pages.size() > 1 ){
            currentPageLabel = new JLabel("1");
            add(currentPageLabel);
            add(new JLabel("/"));
            add(new JLabel("" + pages.size()));
            gotoPrevLink = PharmaUtil.makeLink("<");
            gotoNextLink = PharmaUtil.makeLink(">");
            add(gotoPrevLink);
            add(gotoNextLink);
        }
        add(new JLabel("(" + patient.lastName + patient.firstName + ")"));
        bind();
    }

    private void bind(){
        gotoPrevLink.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if( currentPage > 0 ){
                    currentPage -= 1;
                    records.showVisits(pages.get(currentPage).visitIds);
                    currentPageLabel.setText("" + (currentPage+1));
                }
            }
        });
        gotoNextLink.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if( currentPage < pages.size() - 1 ){
                    currentPage += 1;
                    records.showVisits(pages.get(currentPage).visitIds);
                    currentPageLabel.setText("" + (currentPage+1));
                }
            }
        });
    }
}
