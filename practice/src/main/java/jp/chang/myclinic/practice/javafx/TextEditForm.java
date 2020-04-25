package jp.chang.myclinic.practice.javafx;

import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.drawer.PaperSize;
import jp.chang.myclinic.drawer.pdf.PdfPrinter;
import jp.chang.myclinic.drawer.printer.PrinterEnv;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.PracticeEnv;
import jp.chang.myclinic.practice.javafx.parts.drawerpreview.DrawerPreviewDialog;
import jp.chang.myclinic.practice.javafx.shohousen.ShohousenInfo;
import jp.chang.myclinic.practice.javafx.shohousen.ShohousenUtil;
import jp.chang.myclinic.romaji.Romaji;
import jp.chang.myclinic.shohousen.ShohousenData;
import jp.chang.myclinic.shohousen.ShohousenDrawer;
import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
    private int textId;
    private String content;
    private TextArea textArea = new TextArea();
    private Callback callback;

    public TextEditForm(TextDTO text) {
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

    public void acquireFocus() {
        textArea.requestFocus();
    }

    private Node createButtons() {
        FlowPane wrapper = new FlowPane();
        Hyperlink enterLink = new Hyperlink("入力");
        Hyperlink cancelLink = new Hyperlink("キャンセル ");
        Hyperlink deleteLink = new Hyperlink("削除");
        Hyperlink shohousenLink = new Hyperlink("処方箋発行");
        Hyperlink shohousenFaxLink = new Hyperlink("FAX処方箋");
        Hyperlink copyLink = new Hyperlink("コピー");
        enterLink.setOnAction(event -> {
            if (callback != null) {
                String content = textArea.getText().trim();
                callback.onEnter(content);
            }
        });
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
        shohousenFaxLink.setOnAction(evt -> doShohousenFax());
        copyLink.setOnAction(evt -> {
            callback.onCopy();
        });
        wrapper.getChildren().addAll(enterLink, cancelLink, deleteLink, shohousenLink, shohousenFaxLink, copyLink);
        return wrapper;
    }

    private CheckBox createBlackWhiteCheckBox(DrawerPreviewDialog dlog, ShohousenData data) {
        CheckBox check = new CheckBox("白黒");
        check.setOnAction(event -> {
            ShohousenDrawer.ShohousenDrawerSettings settings =
                    new ShohousenDrawer.ShohousenDrawerSettings();
            if (check.isSelected()) {
                settings.setColor(0, 0, 0);
            }
            ShohousenDrawer drawer = new ShohousenDrawer(settings);
            data.applyTo(drawer);
            dlog.clearCanvas();
            dlog.setOps(drawer.getOps());
        });
        return check;
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
                        if (patient != null) {
                            data.setFutanWari(info.getHoken(), patient, visitedAt);
                        }
                        data.setKoufuDate(visitedAt);
                        data.setDrugs(content);
                        data.applyTo(drawer);
                        DrawerPreviewDialog previewDialog = new DrawerPreviewDialog() {
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
        } catch (Exception ex) {
            logger.error("Failed to print shohousen.", ex);
            GuiUtil.alertException("処方箋の印刷に失敗しました。", ex);
        }
    }

    private ShohousenDrawer.ShohousenDrawerSettings blackWhiteSettings() {
        ShohousenDrawer.ShohousenDrawerSettings settings =
                new ShohousenDrawer.ShohousenDrawerSettings();
        settings.setColor(0, 0, 0);
        return settings;
    }

    private void doShohousenFax() {
        if (PracticeEnv.INSTANCE.getCurrentVisitId() != visitId) {
            if (!GuiUtil.confirm("現在診察中ではありません。\n本当に。この処方箋を FAX 用に印刷しますか？")) {
                return;
            }
        } else {
            if (!GuiUtil.confirm("処方箋を FAX 用に保存しますか？")) {
                return;
            }
        }
        try {
            ShohousenData data = new ShohousenData();
            PracticeEnv practiceEnv = PracticeEnv.INSTANCE;
            data.setClinicInfo(practiceEnv.getClinicInfo());
            PatientDTO patient = practiceEnv.getCurrentPatient();
            data.setPatient(patient);
            ShohousenInfo.load(visitId)
                    .thenAccept(info -> Platform.runLater(() -> {
                        data.setHoken(info.getHoken());
                        LocalDate visitedAt = LocalDate.parse(info.getVisit().visitedAt.substring(0, 10));
                        data.setFutanWari(info.getHoken(), patient, visitedAt);
                        data.setKoufuDate(visitedAt);
                        data.setDrugs(content);
                        ShohousenDrawer drawer = new ShohousenDrawer();
                        data.applyTo(drawer);
                        double shrink = 2.0;
                        PdfPrinter pdfPrinter = new PdfPrinter(PaperSize.A5);
                        pdfPrinter.setShrink(shrink);
                        try {
                            String savePath = faxShohousenSavePath(patient, textId, info.getVisit(), "");
                            pdfPrinter.print(List.of(drawer.getOps()), savePath);
                        } catch (Exception ex) {
                            GuiUtil.alertException("PdfWriter error", ex);
                        }
                        drawer = new ShohousenDrawer(blackWhiteSettings());
                        data.applyTo(drawer);
                        pdfPrinter = new PdfPrinter(PaperSize.A4);
                        pdfPrinter.setShrink(shrink);
                        try {
                            String savePath = faxShohousenSavePath(patient, textId, info.getVisit(), "-bw");
                            pdfPrinter.print(List.of(drawer.getOps()), savePath);
                            String stampedPath = faxShohousenSavePath(patient, textId, info.getVisit(), "-stamped");
                            stamp(savePath, stampedPath);
                        } catch (Exception ex) {
                            GuiUtil.alertException("PdfWriter error", ex);
                        }
                        callback.onDone();
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        } catch (Exception ex) {
            logger.error("Failed to print shohousen.", ex);
            GuiUtil.alertException("処方箋の印刷に失敗しました。", ex);
        }
    }

    private final static DateTimeFormatter shohousenSubfolderFormatter = DateTimeFormatter.ofPattern("yyyy-MM");

    private String shohousenSaveDir(LocalDate date) {
        String base = System.getenv("MYCLINIC_SHOHOUSEN_DIR");
        if (base == null) {
            throw new RuntimeException("Cannot find env var: MYCLINIC_SHOHOUSEN_DIR");
        }
        String sub = date.format(shohousenSubfolderFormatter);
        Path path = Paths.get(base, sub);
        ensureDirectory(path);
        return path.toString();
    }

    private void ensureDirectory(Path path) {
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException ex) {
                GuiUtil.alertException("createDirectory error", ex);
            }
        }
    }

    private String faxShohousenSavePath(PatientDTO patient, int textId, VisitDTO visit, String tail) {
        String name = Romaji.toRomaji(patient.lastNameYomi + patient.firstNameYomi);
        LocalDateTime visitedAt = DateTimeUtil.parseSqlDateTime(visit.visitedAt);
        String stamp = visitedAt.format(DateTimeFormatter.BASIC_ISO_DATE);
        String file = String.format("%s-%d-%d-%s%s.pdf", name, textId, patient.patientId, stamp, tail);
        return Paths.get(shohousenSaveDir(visitedAt.toLocalDate()), file).toString();
    }

    private String getGrayStampPath() {
        return Paths.get(System.getenv("MYCLINIC_CONFIG"), "hanko-gray.png").toString();
    }

    private String getStampPath() {
        return Paths.get(System.getenv("MYCLINIC_CONFIG"), "hanko.png").toString();
    }

    private void stamp(String origPath, String outPath) {
        try {
            PdfReader reader = new PdfReader(new FileInputStream(origPath));
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outPath));
            Image img = Image.getInstance(getGrayStampPath());
            img.scalePercent(65);
            img.setAbsolutePosition(340, 708);
            stamper.getOverContent(1).addImage(img);
            stamper.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
