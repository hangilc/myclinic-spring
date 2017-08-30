package jp.chang.myclinic.practice.leftpane;

import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitFull2PageDTO;
import jp.chang.myclinic.practice.MainExecContext;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LeftPaneWrapper extends JPanel implements LeftPaneContext {

    private MainExecContext mainExecContext;
    private PatientInfoPane patientInfoPane;
    private RecordsNav topNav;
    private DispRecords dispRecords;
    private JScrollPane dispScroll;
    private RecordsNav botNav;

    public LeftPaneWrapper(MainExecContext mainExecContext){
        setLayout(new MigLayout("insets 0", "[grow]", "[] [] [grow] []"));
        this.mainExecContext = mainExecContext;
        EventQueue.invokeLater(this::setupComponents);
    }

    private void setupComponents(){
        int width = getSize().width;
        this.patientInfoPane = new PatientInfoPane(width);
        this.topNav = makeNav();
        this.botNav = makeNav();
        dispRecords = new DispRecords(width, mainExecContext);
        dispScroll = new JScrollPane(dispRecords);
        dispScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        dispScroll.setBorder(null);
        dispScroll.getVerticalScrollBar().setUnitIncrement(16);
        dispScroll.getVerticalScrollBar().setBlockIncrement(160);
        add(this.patientInfoPane, "wrap");
        add(topNav, "wrap");
        add(dispScroll, "grow, wrap");
        add(botNav, "");
        revalidate();
    }

    public void reset(){
        dispRecords.clear();
        setVisible(false);
        repaint();
        revalidate();
    }

    public void start(VisitFull2PageDTO page){
        PatientDTO patient = mainExecContext.getCurrentPatient();
        patientInfoPane.setPatient(patient);
        topNav.start(patient.patientId, page.totalPages);
        botNav.start(patient.patientId, page.totalPages);
        dispRecords.setVisits(page.visits);
        EventQueue.invokeLater(() -> {
            dispScroll.getVerticalScrollBar().setValue(0);
            setVisible(true);
            repaint();
            revalidate();
        });
    }

    private RecordsNav makeNav(){
        return new RecordsNav(){
            @Override
            void changePage(VisitFull2PageDTO visitFullPage, int page) {
                dispRecords.setVisits(visitFullPage.visits);
                topNav.set(page, visitFullPage.totalPages);
                botNav.set(page, visitFullPage.totalPages);
            }
        };
    }

    @Override
    public CompletableFuture<Boolean> onDrugsEntered(int visitId, List<DrugFullDTO> drugs) {
        dispRecords.appendDrugs(visitId, drugs);
        return CompletableFuture.completedFuture(true);
    }
}
