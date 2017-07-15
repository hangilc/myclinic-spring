package jp.chang.myclinic.pharma.rightpane;

import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.pharma.PharmaUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

class AuxRecordsNav extends JPanel {

    private AuxDispRecords records;
    private int currentPage;
    private List<RecordPage> pages;
    private JLabel currentPageLabel;
    private JLabel gotoPrevLink;
    private JLabel gotoNextLink;

    AuxRecordsNav(AuxDispRecords records, List<RecordPage> pages, PatientDTO patient){
        this.records = records;
        this.pages = pages;
        setLayout(new MigLayout("insets 0", "", ""));
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
        bind();
    }

    void updateVisits(){
        if( currentPage >= 0 && currentPage < pages.size() ){
            records.showVisits(pages.get(currentPage).visitIds);
        }
    }

    private void bind(){
        if( gotoPrevLink != null ) {
            gotoPrevLink.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (currentPage > 0) {
                        currentPage -= 1;
                        records.showVisits(pages.get(currentPage).visitIds);
                        currentPageLabel.setText("" + (currentPage + 1));
                    }
                }
            });
        }
        if( gotoNextLink != null ) {
            gotoNextLink.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (currentPage < pages.size() - 1) {
                        currentPage += 1;
                        records.showVisits(pages.get(currentPage).visitIds);
                        currentPageLabel.setText("" + (currentPage + 1));
                    }
                }
            });
        }
    }
}
