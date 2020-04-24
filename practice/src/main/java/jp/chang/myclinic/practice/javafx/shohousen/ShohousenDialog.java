package jp.chang.myclinic.practice.javafx.shohousen;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.consts.Sex;
import jp.chang.myclinic.drawer.PaperSize;
import jp.chang.myclinic.drawer.printer.PrinterEnv;
import jp.chang.myclinic.dto.ClinicInfoDTO;
import jp.chang.myclinic.practice.PracticeEnv;
import jp.chang.myclinic.practice.javafx.parts.DispGrid;
import jp.chang.myclinic.practice.javafx.parts.SexInput;
import jp.chang.myclinic.practice.javafx.parts.drawerpreview.DrawerPreviewDialog;
import jp.chang.myclinic.util.kanjidate.Gengou;
import jp.chang.myclinic.util.logic.ErrorMessages;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.RadioButtonGroup;
import jp.chang.myclinic.utilfx.dateinput.DateForm;
import jp.chang.myclinic.utilfx.dateinput.DateFormInputs;
import jp.chang.myclinic.utilfx.dateinput.DateFormLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jp.chang.myclinic.shohousen.ShohousenData;
import jp.chang.myclinic.shohousen.ShohousenDrawer;

import java.time.LocalDate;
import java.util.List;

public class ShohousenDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(ShohousenDialog.class);

    public enum HokenKubun {
        Hihokensha,
        Hifuyousha
    }

    private PrinterEnv printerEnv;
    private final TextField patientNameInput = new TextField();
    private final DateForm birthdayForm = new DateForm(Gengou.values(), Gengou.Shouwa);
    private final SexInput sexInput = new SexInput(Sex.Female);
    private final RadioButtonGroup<HokenKubun> hokenKubunInput = new RadioButtonGroup<>();
    private final TextField futanWariInput = new TextField();
    private final DateForm issueDateForm = new DateForm(Gengou.Recent, Gengou.Current);
    private final TextField hokenshaInput = new TextField();
    private final TextField kigouBangouInput = new TextField();
    private final TextField kouhiFutansha1 = new TextField();
    private final TextField kouhiJukyuusha1 = new TextField();
    private final TextField kouhiFutansha2 = new TextField();
    private final TextField kouhiJukyuusha2 = new TextField();
    private final TextField doctorNameInput = new TextField("");
    private final TextField postalCodeInput = new TextField("〒");
    private final TextField addressInput = new TextField();
    private final TextField clinicNameInput = new TextField();
    private final TextField phoneInput = new TextField();
    private final TextField kikancodeInput = new TextField();
    private final TextArea contentInput = new TextArea();
    private final CheckBox bwCheckBox = new CheckBox();

    public ShohousenDialog() {
        VBox root = new VBox(4);
        root.getStyleClass().add("shohousen-dialog");
        root.getStylesheets().add("css/ShohousenDialog.css");
        root.getChildren().addAll(
                createCommands(),
                createInputs()
                );
        setScene(new Scene(root));
    }

    public void setPrinterEnv(PrinterEnv printerEnv) {
        this.printerEnv = printerEnv;
    }

    public void setDoctorName(String doctorName){
        doctorNameInput.setText(doctorName);
    }

    public void setClinicInfo(ClinicInfoDTO clinicInfo){
        {
            String postalCode = clinicInfo.postalCode;
            if( postalCode != null && postalCode.startsWith("〒") ){
                postalCode = postalCode.substring(1);
            }
            postalCodeInput.setText(postalCode);
        }
        addressInput.setText(clinicInfo.address);
        phoneInput.setText(clinicInfo.tel);
        clinicNameInput.setText(clinicInfo.name);
        String kikancode = ShohousenUtil.composeFullKikanCode(clinicInfo);
        kikancodeInput.setText(kikancode);
    }

    private Node createInputs(){
        DispGrid grid = new DispGrid();
        grid.rightAlignFirstColumn();
        patientNameInput.getStyleClass().add("patient-name-input");
        grid.addRow("患者氏名", patientNameInput);
        addBirthdayInput(grid);
        grid.addRow("性別", sexInput);
        addHokenKubunInput(grid);
        futanWariInput.getStyleClass().add("futan-wari-input");
        grid.addRow("負担割", futanWariInput, new Label("割"));
        addIssueDateInput(grid);
        hokenshaInput.getStyleClass().add("hokensha-input");
        grid.addRow("保険者番号", hokenshaInput);
        kigouBangouInput.getStyleClass().add("kigou-bangou-input");
        grid.addRow("記号・番号", kigouBangouInput);
        doctorNameInput.getStyleClass().add("doctor-name-input");
        kouhiFutansha1.getStyleClass().add("kouhi-futansha-input");
        grid.addRow("公費負担者１", kouhiFutansha1);
        kouhiJukyuusha1.getStyleClass().add("kouhi-jukyuusha-input");
        grid.addRow("公費受給者１", kouhiJukyuusha1);
        kouhiFutansha2.getStyleClass().add("kouhi-futansha-input");
        grid.addRow("公費負担者２", kouhiFutansha2);
        kouhiJukyuusha2.getStyleClass().add("kouhi-jukyuusha-input");
        grid.addRow("公費受給者２", kouhiJukyuusha2);
        addContentInput(grid);
        grid.addRow("医師名", doctorNameInput);
        addHakkouKikan(grid);
        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.getStyleClass().add("input-scroll");
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }

    private void addContentInput(DispGrid grid){
        contentInput.setWrapText(true);
        contentInput.getStyleClass().add("drugs-input");
        //contentInput.setFont(new Font("MS Gothic", 13));
        grid.addRow("薬剤", contentInput);
    }

    private void addIssueDateInput(DispGrid grid){
        issueDateForm.setDateFormInputs(DateFormLogic.localDateToDateFormInputs(LocalDate.now()));
        grid.addRow("発行日", issueDateForm);
    }

    private void addHokenKubunInput(DispGrid grid){
        hokenKubunInput.createRadioButton("被保険者", HokenKubun.Hihokensha);
        hokenKubunInput.createRadioButton("被扶養者", HokenKubun.Hifuyousha);
        HBox hbox = new HBox(4);
        hbox.getChildren().addAll(hokenKubunInput.getButtons());
        hokenKubunInput.setValue(HokenKubun.Hihokensha);
        grid.addRow("保険区分", hbox);
    }

    private void addBirthdayInput(DispGrid grid){
        grid.addRow("生年月日", birthdayForm);
    }

    private void addHakkouKikan(DispGrid grid){
        postalCodeInput.getStyleClass().add("postalcode-input");
        phoneInput.getStyleClass().add("phone-input");
        kikancodeInput.getStyleClass().add("kikancode-input");
        grid.addRow("郵便番号", postalCodeInput);
        grid.addRow("住所", addressInput);
        grid.addRow("電話番号", phoneInput);
        grid.addRow("医院名", clinicNameInput);
        grid.addRow("機関コード", kikancodeInput);
    }

    private Node createCommands(){
        HBox hbox = new HBox(4);
        Button previewButton = new Button("プレビュー");
        previewButton.setOnAction(evt -> doPreview());
        hbox.getChildren().addAll(
                previewButton
        );
        return hbox;
    }

    private void doPreview(){
        try {
            ShohousenDrawer drawer = new ShohousenDrawer();
            setDrawerHakkouKikan(drawer);
            drawer.setDoctorName(doctorNameInput.getText());
            drawer.setShimei(patientNameInput.getText());
            if( !setDrawerBirthday(drawer) ){
                return;
            }
            setDrawerSex(drawer);
            setDrawerHokenKubun(drawer);
            if( !setDrawerFutanWari(drawer) ){
                return;
            }
            if( !setDrawerIssueDate(drawer) ){
                return;
            }
            if( !setDrawerHokensha(drawer) ){
                return;
            }
            drawer.setHihokensha(kigouBangouInput.getText());
            if( !(setKouhiFutansha1(drawer) && setKouhiJukyuusha1(drawer)) ){
                return;
            }
            if( !(setKouhiFutansha2(drawer) && setKouhiJukyuusha2(drawer)) ){
                return;
            }
            setDrugs(drawer);
            String settingName = PracticeEnv.INSTANCE.getAppProperty(PracticeEnv.SHOHOUSEN_PRINTER_SETTING_KEY);
            DrawerPreviewDialog previewDialog = new DrawerPreviewDialog(printerEnv){
                @Override
                protected void onDefaultSettingChange(String newSettingName) {
                    ShohousenUtil.changeDefaultPrinterSetting(newSettingName);
                }
            };
            previewDialog.setDefaultPrinterSetting(settingName);
            previewDialog.setScaleFactor(0.8);
            previewDialog.setContentSize(PaperSize.A5);
            previewDialog.setOps(drawer.getOps());
            previewDialog.showAndWait();
        } catch(Exception ex){
            logger.error("Failed to preview shohousen.", ex);
            GuiUtil.alertException("処方箋のプレビューに失敗しました。", ex);
        }
    }

    private void setDrugs(ShohousenDrawer drawer){
        String src = contentInput.getText().trim();
        String[] lines = src.split("\\r\\n|\\n|\\r");
        drawer.setDrugLinesAndMemo(List.of(lines), "");
    }

    private boolean setKouhiFutansha1(ShohousenDrawer drawer){
        String input = kouhiFutansha1.getText();
        if( input.isEmpty() ){
            return true;
        }
        try {
            int num = Integer.parseInt(input);
            String rep = "" + num;
            if( rep.length() > 8 ){
                GuiUtil.alertError("公費負担者番号の大きさは８桁までです。");
                return false;
            }
            drawer.setKouhi1Futansha(rep);
            return true;
        } catch(Exception ex){
            GuiUtil.alertError("公費負担者番号の入力が不適切です。");
            return false;
        }

    }

    private boolean setKouhiFutansha2(ShohousenDrawer drawer){
        String input = kouhiFutansha2.getText();
        if( input.isEmpty() ){
            return true;
        }
        try {
            int num = Integer.parseInt(input);
            String rep = "" + num;
            if( rep.length() > 8 ){
                GuiUtil.alertError("公費負担者番号の大きさは８桁までです。");
                return false;
            }
            drawer.setKouhi2Futansha(rep);
            return true;
        } catch(Exception ex){
            GuiUtil.alertError("公費負担者番号の入力が不適切です。");
            return false;
        }

    }

    private boolean setKouhiJukyuusha1(ShohousenDrawer drawer){
        String input = kouhiJukyuusha1.getText();
        if( input.isEmpty() ){
            return true;
        }
        try {
            int num = Integer.parseInt(input);
            String rep = "" + num;
            if( rep.length() > 7 ){
                GuiUtil.alertError("公費受給者番号の大きさは７桁までです。");
                return false;
            }
            drawer.setKouhi1Jukyuusha(rep);
            return true;
        } catch(Exception ex){
            GuiUtil.alertError("公費受給者番号の入力が不適切です。");
            return false;
        }

    }

    private boolean setKouhiJukyuusha2(ShohousenDrawer drawer){
        String input = kouhiJukyuusha2.getText();
        if( input.isEmpty() ){
            return true;
        }
        try {
            int num = Integer.parseInt(input);
            String rep = "" + num;
            if( rep.length() > 7 ){
                GuiUtil.alertError("公費受給者番号の大きさは７桁までです。");
                return false;
            }
            drawer.setKouhi2Jukyuusha(rep);
            return true;
        } catch(Exception ex){
            GuiUtil.alertError("公費受給者番号の入力が不適切です。");
            return false;
        }

    }

    private boolean setDrawerHokensha(ShohousenDrawer drawer){
        String input = hokenshaInput.getText();
        if( input.isEmpty() ){
            return true;
        }
        try {
            int num = Integer.parseInt(input);
            String rep = "" + num;
            if( rep.length() > 8 ){
                GuiUtil.alertError("保険者番号の大きさは８桁までです。");
                return false;
            }
            drawer.setHokenshaBangou(rep);
            return true;
        } catch(Exception ex){
            GuiUtil.alertError("保険者番号の入力が不適切です。");
            return false;
        }
    }

    private boolean setDrawerFutanWari(ShohousenDrawer drawer){
        if( futanWariInput.getText().isEmpty() ){
            return true;
        }
        try {
            int futanWari = Integer.parseInt(futanWariInput.getText());
            drawer.setFutanWari(futanWari);
            return true;
        } catch(Exception ex){
            GuiUtil.alertError("負担割の入力が不適切です。");
            return false;
        }
    }

    private void setDrawerHokenKubun(ShohousenDrawer drawer){
        if( hokenKubunInput.getValue() == HokenKubun.Hihokensha ){
            drawer.setKubunHihokensha();
        } else if( hokenKubunInput.getValue() == HokenKubun.Hifuyousha ){
            drawer.setKubunHifuyousha();
        }
    }

    private void setDrawerSex(ShohousenDrawer drawer){
        if( sexInput.getValue() == Sex.Male ){
            drawer.setSexMale();
        } else if( sexInput.getValue() == Sex.Female ){
            drawer.setSexFemale();
        }
    }

    private boolean setDrawerBirthday(ShohousenDrawer drawer){
        if( birthdayForm.isEmpty() ){
            return true;
        }
        ErrorMessages em = new ErrorMessages();
        DateFormInputs inputs = birthdayForm.getDateFormInputs();
        LocalDate birthday = DateFormLogic.dateFormInputsToLocalDate(inputs, "生年月日", em);
        if( em.hasNoError() && birthday != null ){
            drawer.setKoufuDate(birthday.getYear(), birthday.getMonthValue(), birthday.getDayOfMonth());
            return true;
        } else {
            GuiUtil.alertError(em.getMessage());
            return false;
        }
    }

    private boolean setDrawerIssueDate(ShohousenDrawer drawer){
        if( issueDateForm.isEmpty() ){
            return true;
        }
        ErrorMessages em = new ErrorMessages();
        DateFormInputs inputs = issueDateForm.getDateFormInputs();
        LocalDate issueDate = DateFormLogic.dateFormInputsToLocalDate(inputs, "発行日", em);
        if( em.hasNoError() && issueDate != null ){
            drawer.setKoufuDate(issueDate.getYear(), issueDate.getMonthValue(), issueDate.getDayOfMonth());
            return true;
        } else {
            GuiUtil.alertError(em.getMessage());
            return false;
        }
    }

    private void setDrawerHakkouKikan(ShohousenDrawer drawer){
        String postalCode = postalCodeInput.getText();
        if( !postalCode.startsWith("〒") ){
            postalCode = "〒" + postalCode;
        }
        String address = postalCode + " " + addressInput.getText();
        drawer.setHakkouKikan(address, clinicNameInput.getText(), phoneInput.getText(),
                kikancodeInput.getText());
    }

}
