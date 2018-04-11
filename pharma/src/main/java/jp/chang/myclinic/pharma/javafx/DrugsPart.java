package jp.chang.myclinic.pharma.javafx;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.drugbag.DrugBagDrawer;
import jp.chang.myclinic.drawer.drugbag.DrugBagDrawerData;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.PharmaDrugDTO;
import jp.chang.myclinic.pharma.DrugBagDataCreator;
import jp.chang.myclinic.pharma.Service;
import jp.chang.myclinic.pharma.javafx.drawerpreview.DrawerPreviewDialog;
import jp.chang.myclinic.pharma.javafx.lib.HandlerFX;
import jp.chang.myclinic.util.DrugUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

class DrugsPart extends VBox {

    private static Logger logger = LoggerFactory.getLogger(DrugsPart.class);

    DrugsPart() {
        super(4);
    }

    void setDrugs(List<DrugFullDTO> drugs) {
        getChildren().clear();
        int index = 1;
        for (DrugFullDTO drug : drugs) {
            getChildren().add(drugRep(index++, drug));
        }
    }

    void reset() {
        getChildren().clear();
    }

    private Node drugRep(int index, DrugFullDTO drug) {
        TextFlow textFlow = new TextFlow();
        textFlow.getChildren().addAll(
                new Text(index + ")"),
                new Text(DrugUtil.drugRep(drug)),
                drugBagLink(drug)
        );
        if( drug.drug.prescribed != 0 ){
            Text prescribed = new Text("処方済");
            prescribed.getStyleClass().add("prescribed");
            textFlow.getChildren().add(prescribed);
        }
        return textFlow;
    }

    private Node drugBagLink(DrugFullDTO drug){
        Hyperlink link = new Hyperlink("薬袋");
        link.getStyleClass().add("drugbag-link");
        link.setOnAction(evt -> doDrugBag(drug));
        return link;
    }

    private void doDrugBag(DrugFullDTO drug){
        class Data {
            private PatientDTO patient;
            private PharmaDrugDTO pharmaDrug;
        }
        Data data = new Data();
        Service.api.getVisit(drug.drug.visitId)
                .thenCompose(visit -> Service.api.getPatient(visit.patientId))
                .thenCompose(patient -> {
                    data.patient = patient;
                    return Service.api.findPharmaDrug(drug.drug.iyakuhincode);
                })
                .thenCompose(pharmaDrug -> {
                    data.pharmaDrug = pharmaDrug;
                    return Service.api.getClinicInfo();
                })
                .thenAccept(clinicInfo -> {
                    DrugBagDataCreator creator = new DrugBagDataCreator(drug, data.patient,
                            data.pharmaDrug, clinicInfo);
                    DrugBagDrawerData drawerData = creator.createData();
                    List<Op> ops = new DrugBagDrawer(drawerData).getOps();
                    Platform.runLater(() -> {
                        DrawerPreviewDialog previewDialog = new DrawerPreviewDialog();
                        previewDialog.setContentSize(128, 182);
                        previewDialog.setScaleFactor(1.0);
                        previewDialog.setOps(ops);
                        previewDialog.show();
                    });
                })
                .exceptionally(HandlerFX::exceptionally);
    }
}
