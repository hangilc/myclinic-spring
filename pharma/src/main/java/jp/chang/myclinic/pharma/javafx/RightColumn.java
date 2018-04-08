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
    private PrescPane prescPane = new PrescPane();
    private AuxSwitch auxSwitch;
    private AuxNav auxNav;
    private Records records;
    private ByDateNav byDateNav;
    private ByDrugNav byDrugNav;

    RightColumn() {
        super(4);
        setVisible(false);
        getStyleClass().add("right-column");
        byDateNav = new ByDateNav(){
            @Override
            public void onPage(List<VisitTextDrugDTO> visits, String hilight) {
                records.setItems(visits, hilight);
            }
        };
        byDrugNav = new ByDrugNav(){

            @Override
            public void onPage(List<VisitTextDrugDTO> visits, String hilight) {
                records.setItems(visits, hilight);
            }
        };
        getChildren().addAll(
                prescPane,
                createAuxSwitch(),
                createAuxNav(),
                createRecords()
        );
    }

    void startPresc(PharmaQueueFullDTO item, List<DrugFullDTO> drugs){
        if( item != null ){
            PatientDTO patient = item.patient;
            String patientName = patientName(item.patient);
            prescPane.setItem(item, drugs);
            byDateNav.setPatient(patient.patientId, patientName);
            byDrugNav.setPatient(patient.patientId, patientName);
            auxNav.setContent(byDateNav);
            byDateNav.trigger();
            setVisible(true);
        } else {
            setVisible(false);
        }
    }

    private Node createAuxSwitch(){
        auxSwitch = new AuxSwitch();
        return auxSwitch;
    }

    private Node createAuxNav(){
        auxNav = new AuxNav();
        return auxNav;
    }

    private Node createRecords(){
        records = new Records();
        return records;
    }

    private String patientName(PatientDTO patient){
        return patient.lastName + patient.firstName;
    }

}
