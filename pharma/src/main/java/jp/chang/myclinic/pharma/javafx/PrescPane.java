package jp.chang.myclinic.pharma.javafx;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.drugbag.DrugBagDrawer;
import jp.chang.myclinic.drawer.presccontent.PrescContentDrawer;
import jp.chang.myclinic.drawer.presccontent.PrescContentDrawerData;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.pharma.*;
import jp.chang.myclinic.pharma.javafx.drawerpreview.DrawerPreviewDialog;
import jp.chang.myclinic.pharma.javafx.drawerpreview.DrawerPreviewDialogEx;
import jp.chang.myclinic.pharma.javafx.lib.HandlerFX;
import jp.chang.myclinic.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

class PrescPane extends VBox {

    private static Logger logger = LoggerFactory.getLogger(PrescPane.class);
    private Text nameText = new Text("");
    private Text yomiText = new Text("");
    private Text infoText = new Text("");
    private DrugsPart drugsPart = new DrugsPart();
    private List<DrugFullDTO> drugs = Collections.emptyList();
    private PatientDTO patient;

    PrescPane() {
        super(4);
        getStyleClass().add("presc-pane");
        nameText.getStyleClass().add("patient-name");
        getChildren().addAll(
                new TextFlow(nameText),
                new TextFlow(yomiText),
                new TextFlow(infoText),
                drugsPart,
                createCommands1(),
                createCommands2(),
                createCommands3()
        );
    }

    void setItem(PharmaQueueFullDTO item, List<DrugFullDTO> drugs){
        PatientDTO patient = item.patient;
        nameText.setText(String.format("%s %s", patient.lastName, patient.firstName));
        yomiText.setText(String.format("%s %s", patient.lastNameYomi, patient.firstNameYomi));
        infoText.setText(infoText(patient));
        drugsPart.setDrugs(drugs);
        this.drugs = drugs;
        this.patient = item.patient;
    }

    void reset(){
        nameText.setText("");
        yomiText.setText("");
        infoText.setText("");
        drugsPart.reset();
        this.drugs = Collections.emptyList();
        this.patient = null;
    }

    private String infoText(PatientDTO patient){
        String birthdayPart = "";
        try {
            LocalDate birthday = DateTimeUtil.parseSqlDate(patient.birthday);
            birthdayPart = String.format("%s生 %d才",
                    DateTimeUtil.toKanji(birthday),
                    DateTimeUtil.calcAge(birthday));
        } catch(Exception ex){
            logger.error("Failed to get birthday.");
        }
        return String.format("患者番号 %d %s %s性",
                patient.patientId,
                birthdayPart,
                "M".equals(patient.sex) ? "男" : "女");
    }

    private Node createCommands1(){
        HBox hbox = new HBox(4);
        Button printPrescButton = new Button("処方内容印刷");
        Button printDrugBagButton = new Button("薬袋印刷");
        Button printTechouButton = new Button("薬手帳印刷");
        printPrescButton.setOnAction(evt -> doPrintPresc());
        printDrugBagButton.setOnAction(evt -> doPrintDrugBag());
        hbox.getChildren().addAll(
                printPrescButton,
                printDrugBagButton,
                printTechouButton
        );
        return hbox;
    }

    private Node createCommands2(){
        HBox hbox = new HBox(4);
        Button printAllButton = new Button("*全部印刷*");
        Button printAllExceptTechouButton = new Button("*全部印刷(薬手帳なし)*");
        hbox.getChildren().addAll(
                printAllButton,
                printAllExceptTechouButton
        );
        return hbox;
    }

    private Node createCommands3(){
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        Button cancelButton = new Button("キャンセル");
        Button doneButton = new Button("薬渡し終了");
        hbox.getChildren().addAll(
                cancelButton,
                doneButton
        );
        return hbox;
    }

    private void doPrintPresc(){
        if( patient != null ){
            PrescContentDataCreator creator = new PrescContentDataCreator(patient, LocalDate.now(), drugs);
            PrescContentDrawerData drawerData = creator.createData();
            List<Op> ops = new PrescContentDrawer(drawerData).getOps();
            DrawerPreviewDialog previewDialog = new DrawerPreviewDialog(Globals.printerEnv){
                @Override
                protected String getDefaultPrinterSettingName() {
                    return Config.load().map(Config::getPrescContentPrinterSetting).orElse(null);
                }

                @Override
                protected void setDefaultPrinterSettingName(String newName) {
                    Config.load().ifPresent(config -> {
                        config.setPrescContentPrinterSetting(newName);
                        config.save();
                    });
                }
            };
            previewDialog.setContentSize(148, 210);
            previewDialog.setScaleFactor(0.8);
            previewDialog.setOps(ops);
            previewDialog.show();
        }
    }

    private class DrugWithPharmaDrug {
        private DrugFullDTO drug;
        private PharmaDrugDTO pharmaDrug;
    }

    private CompletableFuture<List<DrugWithPharmaDrug>> collectPharmaDrugs(List<DrugFullDTO> drugs){
        return CFUtil.map(drugs, drug -> Service.api.getPharmaDrug(drug.drug.iyakuhincode)
                .thenApply(pharmaDrug -> {
                    DrugWithPharmaDrug result = new DrugWithPharmaDrug();
                    result.pharmaDrug = pharmaDrug;
                    result.drug = drug;
                    return result;
                }));
    }

    private void doPrintDrugBag(){
        class TaggedPage {
            private boolean prescribed;
            private List<Op> ops;
            private TaggedPage(boolean prescribed, List<Op> ops){
                this.prescribed = prescribed;
                this.ops = ops;
            }
        }
        if( drugs.size() > 0 ){
            ClinicInfoDTO clinicInfo = Globals.clinicInfo;
            collectPharmaDrugs(drugs)
                    .thenAccept(dps -> Platform.runLater(() -> {
                        List<TaggedPage> pages = dps.stream()
                                .map(dp -> {
                                    DrugBagDataCreator creator = new DrugBagDataCreator(dp.drug, patient,
                                            dp.pharmaDrug, clinicInfo);
                                    DrugBagDrawer drawer = new DrugBagDrawer(creator.createData());
                                    List<Op> ops = drawer.getOps();
                                    Boolean prescribed = dp.drug.drug.prescribed != 0;
                                    return new TaggedPage(prescribed, ops);
                                }).collect(Collectors.toList());
                        List<List<Op>> allPages = pages.stream().map(p -> p.ops).collect(Collectors.toList());
                        List<List<Op>> unprescribedPages = pages.stream()
                                .filter(p -> !p.prescribed).map(p -> p.ops).collect(Collectors.toList());
                        DrawerPreviewDialogEx previewDialog = new DrawerPreviewDialogEx(
                                Globals.printerEnv, 128, 182, 1.0){
                            @Override
                            protected String getDefaultPrinterSettingName() {
                                return Config.load().map(Config::getDrugBagPrinterSetting).orElse(null);
                            }

                            @Override
                            protected void setDefaultPrinterSettingName(String newName) {
                                Config.load()
                                        .ifPresent(config -> {
                                            config.setDrugBagPrinterSetting(newName);
                                            config.save();
                                        });
                            }
                        };
                        previewDialog.addStylesheet("Pharma.css");
                        CheckBox unprescribedOnlyCheck = new CheckBox("処方済も含める");
                        unprescribedOnlyCheck.setSelected(true);
                        unprescribedOnlyCheck.selectedProperty().addListener((obs, oldValue, newValue) -> {
                            if( newValue ){
                                previewDialog.setPages(allPages);
                            } else {
                                previewDialog.setPages(unprescribedPages);
                            }
                        });
                        previewDialog.addToCommands(unprescribedOnlyCheck);
                        previewDialog.setPages(unprescribedPages);
                        previewDialog.show();
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        }
    }




}
