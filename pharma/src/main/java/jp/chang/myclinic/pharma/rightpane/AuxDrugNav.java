package jp.chang.myclinic.pharma.rightpane;

import jp.chang.myclinic.pharma.wrappedtext.WrappedText;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

class AuxDrugNav extends JPanel {

    interface Callbacks {
        void onShowRecords(List<Integer> visitIds);
        void onBackToDrugList();
    }

    private AuxRecordsNav recNav;

    AuxDrugNav(String name, int width, List<RecordPage> pages, Callbacks callbacks){
        setLayout(new MigLayout("insets 0", "", ""));
        WrappedText drugName = new WrappedText(name, width);
        add(drugName, "wrap");
        recNav = new AuxRecordsNav(pages, page -> callbacks.onShowRecords(page.getVisitIds()));
        add(recNav, "wrap");
        JButton backToListButton = new JButton("薬剤一覧にもどえる");
        backToListButton.addActionListener(((ActionEvent event) -> {
            callbacks.onBackToDrugList();
        }));
        add(backToListButton);
    }

    void trigger(){
        recNav.trigger();
    }

}
