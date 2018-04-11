package jp.chang.myclinic.pharma.javafx;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.presccontent.PrescContentDrawer;
import jp.chang.myclinic.drawer.presccontent.PrescContentDrawerData;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.PharmaQueueFullDTO;
import jp.chang.myclinic.pharma.Config;
import jp.chang.myclinic.pharma.Globals;
import jp.chang.myclinic.pharma.PrescContentDataCreator;
import jp.chang.myclinic.pharma.javafx.drawerpreview.DrawerPreviewDialog;
import jp.chang.myclinic.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

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

}
