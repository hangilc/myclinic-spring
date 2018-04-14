package jp.chang.myclinic.pharma.javafx;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.PharmaQueueFullDTO;
import jp.chang.myclinic.dto.VisitTextDrugDTO;
import jp.chang.myclinic.pharma.javafx.records.Records;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

class RightColumn extends VBox {

    private static Logger logger = LoggerFactory.getLogger(RightColumn.class);
    private PrescPane prescPane;
    private AuxSwitch auxSwitch;
    private AuxNav auxNav;
    private Records records;
    private ByDateNav byDateNav;
    private ByDrugNav byDrugNav;

    RightColumn() {
        super(4);
        getStyleClass().add("right-column");
        setVisible(false);
        byDateNav = new ByDateNav() {
            @Override
            public void onPage(List<VisitTextDrugDTO> visits, String hilight) {
                records.setItems(visits, hilight);
            }
        };
        byDrugNav = new ByDrugNav() {
            @Override
            public void onPage(List<VisitTextDrugDTO> visits, String hilight) {
                records.setItems(visits, hilight);
            }
        };
        getChildren().addAll(
                createPrescPane(),
                createAuxSwitch(),
                createAuxNav(),
                createRecords()
        );
    }

    void startPresc(PharmaQueueFullDTO item, List<DrugFullDTO> drugs) {
        PatientDTO patient = item.patient;
        String patientName = patientName(item.patient);
        prescPane.setItem(item, drugs);
        byDateNav.setPatient(patient.patientId, patientName);
        byDrugNav.setPatient(patient.patientId, patientName);
        auxSwitch.reset();
        auxSwitch.trigger();
        setVisible(true);
    }

    private void endPresc(){
        prescPane.reset();
        byDateNav.reset();
        byDrugNav.reset();
        setVisible(false);
    }

    private Node createPrescPane(){
        prescPane = new PrescPane(){
            @Override
            protected void onCancel() {
                endPresc();
                RightColumn.this.onCancel();
            }

            @Override
            protected void onPrescDone() {
                endPresc();
                RightColumn.this.onPrescDone();
            }
        };
        return prescPane;
    }

    void onCancel(){

    }

    void onPrescDone(){

    }

    private Node createAuxSwitch() {
        auxSwitch = new AuxSwitch(){
            @Override
            void onChange(AuxMode mode) {
                if( mode == AuxMode.ByDate ){
                    auxNav.setContent(byDateNav);
                    byDateNav.trigger();
                } else if( mode == AuxMode.ByDrug ){
                    auxNav.setContent(byDrugNav);
                    byDrugNav.trigger();
                }
            }
        };
        return auxSwitch;
    }

    private Node createAuxNav() {
        auxNav = new AuxNav();
        return auxNav;
    }

    private Node createRecords() {
        records = new Records();
        return records;
    }

    private String patientName(PatientDTO patient) {
        return patient.lastName + patient.firstName;
    }

}
