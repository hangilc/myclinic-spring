package jp.chang.myclinic.pharma.javafx;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.drugbag.DrugBagDrawer;
import jp.chang.myclinic.drawer.drugbag.DrugBagDrawerData;
import jp.chang.myclinic.dto.ClinicInfoDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.pharma.Globals;
import jp.chang.myclinic.pharma.drawercreator.DrugBagDataCreator;
import jp.chang.myclinic.pharma.javafx.drawerpreview.DrawerPreviewDialog;
import jp.chang.myclinic.util.DrugUtil;
import jp.chang.myclinic.utilfx.HandlerFX;
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
        }
        Data data = new Data();
        Service.api.getVisit(drug.drug.visitId)
                .thenCompose(visit -> Service.api.getPatient(visit.patientId))
                .thenCompose(patient -> {
                    data.patient = patient;
                    return Service.api.findPharmaDrug(drug.drug.iyakuhincode);
                })
                .thenAccept(pharmaDrug -> {
                    ClinicInfoDTO clinicInfo = Globals.getClinicInfo();
                    DrugBagDataCreator creator = new DrugBagDataCreator(drug, data.patient,
                            pharmaDrug, clinicInfo);
                    DrugBagDrawerData drawerData = creator.createData();
                    List<Op> ops = new DrugBagDrawer(drawerData).getOps();
                    Platform.runLater(() -> {
                        DrawerPreviewDialog previewDialog = new DrawerPreviewDialog(Globals.getPrinterEnv(),
                                128, 182, 0.6){
                            @Override
                            protected String getDefaultPrinterSettingName() {
                                return Globals.getDrugBagPrinterSetting();
                                //return Config.load().map(Config::getDrugBagPrinterSetting).orElse(null);
                            }

                            @Override
                            protected void setDefaultPrinterSettingName(String newName) {
                                Globals.setDrugBagPrinterSetting(newName);
//                                Config.load()
//                                        .ifPresent(config -> {
//                                            config.setDrugBagPrinterSetting(newName);
//                                            config.save();
//                                        });
                            }
                        };
                        previewDialog.setTitle("薬袋印刷");
                        previewDialog.addStylesheet("Pharma.css");
                        previewDialog.setSinglePage(ops);
                        previewDialog.show();
                    });
                })
                .exceptionally(HandlerFX::exceptionally);
    }
}
