package jp.chang.myclinic.practice.javafx;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.drawer.PaperSize;
import jp.chang.myclinic.drawer.printer.PrinterEnv;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.practice.PracticeEnv;
import jp.chang.myclinic.practice.javafx.parts.drawerpreview.DrawerPreviewDialog;
import jp.chang.myclinic.practice.javafx.shohousen.ShohousenData;
import jp.chang.myclinic.practice.javafx.shohousen.ShohousenDrawer;
import jp.chang.myclinic.practice.javafx.shohousen.ShohousenInfo;
import jp.chang.myclinic.practice.javafx.shohousen.ShohousenUtil;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class TextEditForm extends VBox {

    private static Logger logger = LoggerFactory.getLogger(TextEditForm.class);

    public interface Callback {
        void onEnter(String content);
        void onCancel();
        void onDelete();
        void onDone();
        void onCopy();
    }

    private int visitId;
    private String content;
    private TextArea textArea = new TextArea();
    private Callback callback;

    public TextEditForm(TextDTO text){
        super(4);
        this.visitId = text.visitId;
        this.content = text.content;
        getStyleClass().addAll("record-text-form", "edit");
        setFillWidth(true);
        textArea.setWrapText(true);
        textArea.setText(text.content);
        getChildren().addAll(
                textArea,
                createButtons()
        );
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    public void acquireFocus(){
        textArea.requestFocus();
    }

    private Node createButtons(){
        FlowPane wrapper = new FlowPane();
        Hyperlink enterLink = new Hyperlink("入力");
        Hyperlink cancelLink = new Hyperlink("キャンセル ");
        Hyperlink deleteLink = new Hyperlink("削除");
        Hyperlink shohousenLink = new Hyperlink("処方箋発行");
        Hyperlink copyLink = new Hyperlink("コピー");
        enterLink.setOnAction(event -> {
            if( callback != null ){
                String content = textArea.getText().trim();
                callback.onEnter(content);
            }
        });
        cancelLink.setOnAction(event -> {
            if( callback != null ){
                callback.onCancel();
            }
        });
        deleteLink.setOnAction(event -> {
            if( GuiUtil.confirm("この文章を削除しますか？") ){
                if( callback != null ){
                    callback.onDelete();
                }
            }
        });
        shohousenLink.setOnAction(evt -> doShohousen());
        copyLink.setOnAction(evt -> {
            callback.onCopy();
        });
        wrapper.getChildren().addAll(enterLink, cancelLink, deleteLink, shohousenLink, copyLink);
        return wrapper;
    }

    private CheckBox createBlackWhiteCheckBox(DrawerPreviewDialog dlog, ShohousenData data){
        CheckBox check = new CheckBox("白黒");
        check.setOnAction(event -> {
            ShohousenDrawer.ShohousenDrawerSettings settings =
                    new ShohousenDrawer.ShohousenDrawerSettings();
            if( check.isSelected() ){
                settings.setColor(0, 0, 0);
            }
            ShohousenDrawer drawer = new ShohousenDrawer(settings);
            data.applyTo(drawer);
            dlog.clearCanvas();
            dlog.setOps(drawer.getOps());
        });
        return check;
    }

    private void doShohousen(){
        if( PracticeEnv.INSTANCE.getCurrentVisitId() != visitId ){
            if( !GuiUtil.confirm("現在診察中ではないですか、この処方箋を発行しますか？") ){
                return;
            }
        }
        try {
            ShohousenDrawer drawer = new ShohousenDrawer();
            ShohousenData data = new ShohousenData();
            PracticeEnv practiceEnv = PracticeEnv.INSTANCE;
            PrinterEnv printerEnv = practiceEnv.getMyclinicEnv().getPrinterEnv();
            data.setClinicInfo(practiceEnv.getClinicInfo());
            PatientDTO patient = practiceEnv.getCurrentPatient();
            if( patient!= null ){
                data.setPatient(patient);
            }
            String settingName = PracticeEnv.INSTANCE.getAppProperty(PracticeEnv.SHOHOUSEN_PRINTER_SETTING_KEY);
            ShohousenInfo.load(visitId)
                    .thenAccept(info -> Platform.runLater(() -> {
                        data.setHoken(info.getHoken());
                        LocalDate visitedAt = LocalDate.parse(info.getVisit().visitedAt.substring(0, 10));
                        data.setFutanWari(info.getHoken(), patient, visitedAt);
                        data.setKoufuDate(visitedAt);
                        data.setDrugs(content);
                        data.applyTo(drawer);
                        DrawerPreviewDialog previewDialog = new DrawerPreviewDialog(){
                            @Override
                            protected void onDefaultSettingChange(String newSettingName) {
                                ShohousenUtil.changeDefaultPrinterSetting(newSettingName);
                            }
                        };
                        CheckBox bwCheck = createBlackWhiteCheckBox(previewDialog, data);
                        previewDialog.setTitle("処方箋印刷");
                        previewDialog.addToCommandHBox(bwCheck);
                        previewDialog.setPrinterEnv(printerEnv);
                        previewDialog.setDefaultPrinterSetting(settingName);
                        previewDialog.setScaleFactor(0.8);
                        previewDialog.setContentSize(PaperSize.A5);
                        previewDialog.setOps(drawer.getOps());
                        previewDialog.showAndWait();
                        callback.onDone();
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        } catch(Exception ex){
            logger.error("Failed to print shohousen.", ex);
            GuiUtil.alertException("処方箋の印刷に失敗しました。", ex);
        }
    }

}
