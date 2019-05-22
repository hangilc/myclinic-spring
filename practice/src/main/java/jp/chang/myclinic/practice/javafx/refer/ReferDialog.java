package jp.chang.myclinic.practice.javafx.refer;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.consts.Sex;
import jp.chang.myclinic.drawer.PaperSize;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.ReferItemDTO;
import jp.chang.myclinic.drawer.printer.PrinterEnv;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.javafx.parts.DispGrid;
import jp.chang.myclinic.practice.javafx.parts.SexInput;
import jp.chang.myclinic.practice.javafx.parts.drawerpreview.DrawerPreviewDialog;
import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.util.kanjidate.KanjiDate;
import jp.chang.myclinic.util.kanjidate.KanjiDateRepBuilder;
import jp.chang.myclinic.utilfx.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class ReferDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(ReferDialog.class);
    private ComboBox<String> titleInput;
    private TextField hospitalInput = new TextField("");
    private TextField sectionInput = new TextField("");
    private TextField doctorInput = new TextField("");
    private TextField patientNameInput = new TextField("");
    private TextField birthdayInput = new TextField("");
    private TextField ageInput = new TextField("");
    private SexInput sexInput = new SexInput();
    private TextField diagnosisInput = new TextField("");
    private TextField issueDateInput = new TextField("");
    private TextArea mainContentInput = new TextArea("");
    private PrinterEnv printerEnv;
    private String defaultPrinterSetting;

    public ReferDialog() {
        setTitle("紹介状作成");
        VBox root = new VBox(4);
        root.getStylesheets().addAll(
                "css/Practice.css"
        );
        root.getStyleClass().add("refer-dialog");
        root.getChildren().addAll(
                createCommands(),
                createPropertiesInput(),
                createMainContent()
        );
        setScene(new Scene(root));
    }

    public void setPatient(PatientDTO patient) {
        patientNameInput.setText(patient.lastName + " " + patient.firstName);
        birthdayInput.setText(composeBirthdayRep(patient.birthday));
        ageInput.setText(composeAgeRep(patient.birthday));
        sexInput.getSelectionModel().select(Sex.fromCode(patient.sex));
    }

    public void setIssueDate(LocalDate date) {
        String s = KanjiDate.toKanji(date);
        issueDateInput.setText(s);
    }

    public void setPrinterEnv(PrinterEnv printerEnv) {
        this.printerEnv = printerEnv;
    }

    public void setDefaultPrinterSetting(String setting) {
        this.defaultPrinterSetting = setting;
    }

    private String composeBirthdayRep(String sqldate) {
        if (sqldate == null || sqldate.isEmpty()) {
            return "";
        } else {
            return new KanjiDateRepBuilder(DateTimeUtil.parseSqlDate(sqldate)).format1().build();
        }
    }

    private String composeAgeRep(String sqldate) {
        try {
            LocalDate bd = LocalDate.parse(sqldate);
            return "" + DateTimeUtil.calcAge(bd);
        } catch (Exception ex) {
            return "";
        }
    }

    private Node createCommands() {
        HBox hbox = new HBox(4);
        Button previewButton = new Button("プレビュー");
        Button registeredButton = new Button("登録先");
        previewButton.setOnAction(evt -> doPreview());
        registeredButton.setOnAction(evt -> doRegistered());
        hbox.getChildren().addAll(previewButton, registeredButton);
        return hbox;
    }

    private Node createPropertiesInput() {
        DispGrid part = new DispGrid();
        part.rightAlignFirstColumn();
        ageInput.getStyleClass().add("age-input");
        part.addRow("タイトル", createTitleInput());
        part.addRow("紹介先", hospitalInput);
        part.addRow("紹介科", sectionInput);
        part.addRow("紹介医師", createHBox(doctorInput, new Label("先生")));
        part.addRow("氏名", patientNameInput);
        part.addRow("生年月日", birthdayInput);
        part.addRow("年齢", createHBox(ageInput, new Label("才")));
        part.addRow("性別", sexInput);
        part.addRow("診断名", diagnosisInput);
        part.addRow("作成日", issueDateInput);
        return part;
    }

    private Node createTitleInput() {
        titleInput = new ComboBox<>();
        titleInput.getStyleClass().add("title-input");
        titleInput.getItems().addAll("紹介状", "ご報告");
        titleInput.setEditable(true);
        titleInput.getSelectionModel().select("紹介状");
        return titleInput;
    }

    private HBox createHBox(Node... children) {
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().addAll(children);
        return hbox;
    }

    private Node createMainContent() {
        mainContentInput.setWrapText(true);
        mainContentInput.setText("いつも大変お世話になっております。");
        return mainContentInput;
    }

    private String composeReferDoctor() {
        String section = sectionInput.getText();
        if (section.isEmpty()) {
            section = "　　　　　　";
        }
        String doctorName = doctorInput.getText();
        if (doctorName.isEmpty()) {
            doctorName = "　　　 　　　";
        }
        return String.format("%s　%s 先生 御机下", section, doctorName);
    }

    private String composePatientName() {
        String name = patientNameInput.getText();
        if (name.isEmpty()) {
            name = "　　　 　　　";
        }
        return String.format("%s 様", name);
    }

    private String composePatientInfo() {
        String birthday = birthdayInput.getText();
        if (birthday.isEmpty()) {
            birthday = "　　   年   月   日";
        }
        String age = ageInput.getText();
        if (age.isEmpty()) {
            age = "    ";
        }
        Sex sex = sexInput.getSelectionModel().getSelectedItem();
        String sexText = sex == null ? "　 " : sex.getKanji();
        return String.format("%s生 %s才 %s性", birthday, age, sexText);
    }

    private void doPreview() {
        ReferDrawer drawer = new ReferDrawer();
        drawer.setTitle(getReferTitle());
        drawer.setReferHospital(hospitalInput.getText());
        drawer.setReferDoctor(composeReferDoctor());
        drawer.setPatientName(composePatientName());
        drawer.setPatientInfo(composePatientInfo());
        drawer.setDiagnosis("診断 " + diagnosisInput.getText());
        drawer.setIssueDate(issueDateInput.getText());
        drawer.setContent(mainContentInput.getText());
        Context.frontend.getClinicInfo()
                .thenAcceptAsync(clinicInfo -> {
                            drawer.setAddress(clinicInfo.postalCode, clinicInfo.address, clinicInfo.tel,
                                    clinicInfo.fax, clinicInfo.name, clinicInfo.doctorName);
                            DrawerPreviewDialog previewDialog = new DrawerPreviewDialog() {
                                @Override
                                protected void onDefaultSettingChange(String newSettingName) {
                                    ReferDialog.this.defaultPrinterSetting = newSettingName;
                                    Context.practiceConfig.setReferPrinterSetting(newSettingName);
                                }
                            };
                            previewDialog.setPrinterEnv(printerEnv);
                            previewDialog.setDefaultPrinterSetting(defaultPrinterSetting);
                            previewDialog.setTitle("紹介状のプレビュー");
                            previewDialog.setScaleFactor(0.5);
                            previewDialog.setContentSize(PaperSize.A4.getWidth(), PaperSize.A4.getHeight());
                            previewDialog.setOps(drawer.getOps());
                            previewDialog.show();
                        },
                        Platform::runLater)
                .exceptionally(HandlerFX.exceptionally(this));
    }

    private void doRegistered() {
        List<ReferItemDTO> referList = Context.referService.listRefer();
        if (referList != null) {
            RegisteredDialog dialog = new RegisteredDialog(referList) {
                @Override
                void onEnter(RegisteredDialog self, ReferItemDTO item) {
                    if (item.hospital != null) {
                        hospitalInput.setText(item.hospital);
                    }
                    if (item.section != null) {
                        sectionInput.setText(item.section);
                    }
                    if (item.doctor != null) {
                        doctorInput.setText(item.doctor);
                    }
                    self.close();
                }
            };
            dialog.showAndWait();
        }
    }

    private String getReferTitle() {
        return titleInput.getSelectionModel().getSelectedItem();
    }
}
