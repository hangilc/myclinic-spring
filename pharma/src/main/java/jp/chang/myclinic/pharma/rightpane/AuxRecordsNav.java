package jp.chang.myclinic.pharma.rightpane;

import jp.chang.myclinic.pharma.PharmaUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

class AuxRecordsNav extends JPanel {

    interface Callbacks {
        void onPageSelected(RecordPage page);
    }

    private List<RecordPage> pages;
    private int currentPage;
    private JLabel currentPageLabel;
    private Callbacks callbacks;

    AuxRecordsNav(List<RecordPage> pages, Callbacks callbacks){
        this.pages = pages;
        this.callbacks = callbacks;
        setLayout(new MigLayout("insets 0", "", ""));
        if( pages.size() > 1 ){
            currentPageLabel = new JLabel("1");
            add(currentPageLabel);
            add(new JLabel("/"));
            add(new JLabel("" + pages.size()));
            JLabel gotoPrevLink = PharmaUtil.makeLink("<");
            gotoPrevLink.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (currentPage > 0) {
                        currentPage -= 1;
                        trigger();
                        currentPageLabel.setText("" + (currentPage + 1));
                    }
                }
            });
            JLabel gotoNextLink = PharmaUtil.makeLink(">");
            gotoNextLink.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (currentPage < pages.size() - 1) {
                        currentPage += 1;
                        trigger();
                        currentPageLabel.setText("" + (currentPage + 1));
                    }
                }
            });
            add(gotoPrevLink);
            add(gotoNextLink);
        }
    }

    private RecordPage getCurrentPage(){
        return pages.get(currentPage);
    }

    void trigger(){
        callbacks.onPageSelected(getCurrentPage());
    }

}
