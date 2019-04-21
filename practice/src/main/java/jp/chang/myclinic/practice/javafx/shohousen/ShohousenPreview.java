package jp.chang.myclinic.practice.javafx.shohousen;

import javafx.application.Platform;
import jp.chang.myclinic.drawer.PaperSize;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.frontend.Frontend;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.javafx.parts.drawerpreview.DrawerPreviewDialog;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

public class ShohousenPreview {

    public static CompletableFuture<DrawerPreviewDialog> create(
            int visitId, String text) {
        return new ShohousenPreview().makePreview(visitId, text);
    }

    private VisitDTO visit;
    private PatientDTO patient;
    private ShohousenData data = new ShohousenData();

    private ShohousenPreview() {
    }

    private CompletableFuture<DrawerPreviewDialog> makePreview(int visitId, String text) {
        Frontend frontend = Context.frontend;
        return frontend.getVisit(visitId)
                .thenCompose(visit -> {
                    this.visit = visit;
                    return frontend.getPatient(visit.patientId);
                })
                .thenCompose(patient -> {
                    this.patient = patient;
                    data.setPatient(patient);
                    return frontend.getClinicInfo();
                })
                .thenCompose(clinicInfo -> {
                    data.setClinicInfo(clinicInfo);
                    return frontend.getHoken(visitId);
                })
                .thenApply(hoken -> {
                    ShohousenDrawer drawer = new ShohousenDrawer();
                    data.setHoken(hoken);
                    LocalDate visitedAt = LocalDate.parse(visit.visitedAt.substring(0, 10));
                    data.setFutanWari(hoken, patient, visitedAt);
                    data.setKoufuDate(visitedAt);
                    data.setDrugs(text);
                    data.applyTo(drawer);
                    return drawer;
                })
                .thenApplyAsync(drawer -> {
                    DrawerPreviewDialog previewDialog = new DrawerPreviewDialog() {
                        @Override
                        protected void onDefaultSettingChange(String newSettingName) {
                            Context.setShohousenPrinterSetting(newSettingName);
                        }
                    };
                    previewDialog.setPrinterEnv(Context.printerEnv);
                    previewDialog.setDefaultPrinterSetting(Context.getShohousenPrinterSetting());
                    previewDialog.setScaleFactor(0.8);
                    previewDialog.setContentSize(PaperSize.A5);
                    previewDialog.setOps(drawer.getOps());
                    return previewDialog;
                }, Platform::runLater);
    }

}
