package jp.chang.myclinic.practice.javafx;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.drawer.PaperSize;
import jp.chang.myclinic.drawer.printer.PrinterEnv;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.practice.PracticeEnv;
import jp.chang.myclinic.practice.javafx.events.TextEnteredEvent;
import jp.chang.myclinic.practice.javafx.parts.drawerpreview.DrawerPreviewDialog;
import jp.chang.myclinic.practice.javafx.shohousen.ShohousenData;
import jp.chang.myclinic.practice.javafx.shohousen.ShohousenDrawer;
import jp.chang.myclinic.practice.javafx.shohousen.ShohousenInfo;
import jp.chang.myclinic.practice.javafx.shohousen.ShohousenUtil;
import jp.chang.myclinic.practice.lib.PracticeUtil;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

import static jp.chang.myclinic.utilfx.GuiUtil.alertExceptionGui;

public class TextEditForm extends VBox {

    private static Logger logger = LoggerFactory.getLogger(TextEditForm.class);

    public interface Callback {
        void onUpdate(TextDTO updated);

        void onCancel();

        void onDelete();

        void onDone();
    }

    private int visitId;
    private int textId;
    private String content;
    private TextArea textArea = new TextArea();
    private Callback callback;

    TextEditForm(TextDTO text) {
        super(4);
        this.visitId = text.visitId;
        this.textId = text.textId;
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

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    int getTextId() {
        return textId;
    }

    private Node createButtons() {
        FlowPane wrapper = new FlowPane();
        Hyperlink enterLink = new Hyperlink("入力");
        Hyperlink cancelLink = new Hyperlink("キャンセル ");
        Hyperlink deleteLink = new Hyperlink("削除");
        Hyperlink shohousenLink = new Hyperlink("処方箋発行");
        Hyperlink copyLink = new Hyperlink("コピー");
        enterLink.setOnAction(event -> doUpdate());
        cancelLink.setOnAction(event -> {
            if (callback != null) {
                callback.onCancel();
            }
        });
        deleteLink.setOnAction(event -> {
            if (GuiUtil.confirm("この文章を削除しますか？")) {
                if (callback != null) {
                    callback.onDelete();
                }
            }
        });
        shohousenLink.setOnAction(evt -> doShohousen());
        copyLink.setOnAction(evt -> doCopy());
        wrapper.getChildren().addAll(enterLink, cancelLink, deleteLink, shohousenLink, copyLink);
        return wrapper;
    }

    private void doUpdate() {
        String newContent = textArea.getText().trim();
        TextDTO newText = new TextDTO();
        newText.textId = textId;
        newText.visitId = visitId;
        newText.content = newContent;
        Service.api.updateText(newText)
                .thenCompose(ok -> Service.api.getText(textId))
                .thenAcceptAsync(entered -> {
                            if (callback != null) {
                                callback.onUpdate(entered);
                            }
                        },
                        Platform::runLater)
                .exceptionally(alertExceptionGui("文章の変更に失敗しました。"));
    }

    private void doCopy(){
        int targetVisitId = PracticeUtil.findCopyTarget(visitId);
        if (targetVisitId != 0) {
            TextDTO newText = new TextDTO();
            newText.visitId = targetVisitId;
            newText.content = content;
            Service.api.enterText(newText)
                    .thenCompose(Service.api::getText)
                    .thenAcceptAsync(enteredText -> {
                        TextEnteredEvent textEnteredEvent = new TextEnteredEvent(enteredText);
                        TextEditForm.this.fireEvent(textEnteredEvent);
                        if( callback != null ){
                            callback.onDone();
                        }
                    }, Platform::runLater)
                    .exceptionally(alertExceptionGui("文章のコピーに失敗しました。"));
        }
    }

    private void doShohousen() {
        if (PracticeEnv.INSTANCE.getCurrentVisitId() != visitId) {
            if (!GuiUtil.confirm("現在診察中ではないですか、この処方箋を発行しますか？")) {
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
            if (patient != null) {
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
                        DrawerPreviewDialog previewDialog = new DrawerPreviewDialog() {
                            @Override
                            protected void onDefaultSettingChange(String newSettingName) {
                                ShohousenUtil.changeDefaultPrinterSetting(newSettingName);
                            }
                        };
                        previewDialog.setPrinterEnv(printerEnv);
                        previewDialog.setDefaultPrinterSetting(settingName);
                        previewDialog.setScaleFactor(0.8);
                        previewDialog.setContentSize(PaperSize.A5);
                        previewDialog.setOps(drawer.getOps());
                        previewDialog.showAndWait();
                        if( callback != null ) {
                            callback.onDone();
                        }
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        } catch (Exception ex) {
            logger.error("Failed to print shohousen.", ex);
            GuiUtil.alertException("処方箋の印刷に失敗しました。", ex);
        }
    }

}
