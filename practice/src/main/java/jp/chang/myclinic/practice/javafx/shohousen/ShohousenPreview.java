package jp.chang.myclinic.practice.javafx.shohousen;

import javafx.application.Platform;
import jp.chang.myclinic.drawer.PaperSize;
import jp.chang.myclinic.dto.ClinicInfoDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.javafx.parts.drawerpreview.DrawerPreviewDialog;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

public class ShohousenPreview {

    static CompletableFuture<DrawerPreviewDialog> create(ShohousenLib lib, int visitId, String text) {
        return new ShohousenPreview(lib).makePreview(visitId, text);
    }

    private ShohousenLib lib;
    private VisitDTO visit;
    private PatientDTO patient;
    private ShohousenData data = new ShohousenData();

    private ShohousenPreview(ShohousenLib lib) {
        this.lib = lib;
    }

    private CompletableFuture<DrawerPreviewDialog> makePreview(int visitId, String text) {
        return lib.getVisit(visitId)
                .thenCompose(visit -> {
                    this.visit = visit;
                    return lib.getPatient(visit.patientId);
                })
                .thenCompose(patient -> {
                    this.patient = patient;
                    data.setPatient(patient);
                    return lib.getClinicInfo();
                })
                .thenCompose(clinicInfo -> {
                    data.setClinicInfo(clinicInfo);
                    return lib.getHoken(visitId);
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
                            lib.setPrinterSetting(newSettingName);
                        }
                    };
                    previewDialog.setPrinterEnv(lib.getPrinterEnv());
                    previewDialog.setDefaultPrinterSetting(lib.getPrinterSetting());
                    previewDialog.setScaleFactor(0.8);
                    previewDialog.setContentSize(PaperSize.A5);
                    previewDialog.setOps(drawer.getOps());
                    return previewDialog;
                }, Platform::runLater);
    }

}
