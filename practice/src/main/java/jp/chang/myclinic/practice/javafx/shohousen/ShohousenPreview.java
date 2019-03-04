package jp.chang.myclinic.practice.javafx.shohousen;

import javafx.application.Platform;
import jp.chang.myclinic.backendasync.BackendAsync;
import jp.chang.myclinic.drawer.PaperSize;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.PracticeConfigService;
import jp.chang.myclinic.practice.javafx.ExecEnv;
import jp.chang.myclinic.practice.javafx.parts.drawerpreview.DrawerPreviewDialog;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

public class ShohousenPreview {

    public static CompletableFuture<DrawerPreviewDialog> create(
            ExecEnv execEnv, int visitId, String text) {
        return new ShohousenPreview(execEnv).makePreview(visitId, text);
    }

    private BackendAsync restService;
    private PracticeConfigService configService;
    private VisitDTO visit;
    private PatientDTO patient;
    private ShohousenData data = new ShohousenData();

    private ShohousenPreview(ExecEnv execEnv) {
        this.restService = execEnv.restService;
        this.configService = execEnv.configService;
    }

    private CompletableFuture<DrawerPreviewDialog> makePreview(int visitId, String text) {
        return restService.getVisit(visitId)
                .thenCompose(visit -> {
                    this.visit = visit;
                    return restService.getPatient(visit.patientId);
                })
                .thenApply(patient -> {
                    this.patient = patient;
                    data.setPatient(patient);
                    return configService.getClinicInfo();
                })
                .thenCompose(clinicInfo -> {
                    data.setClinicInfo(clinicInfo);
                    return restService.getHoken(visitId);
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
                            configService.setShohousenPrinterSetting(newSettingName);
                        }
                    };
                    previewDialog.setPrinterEnv(configService.getPrinterEnv());
                    previewDialog.setDefaultPrinterSetting(configService.getShohousenPrinterSetting());
                    previewDialog.setScaleFactor(0.8);
                    previewDialog.setContentSize(PaperSize.A5);
                    previewDialog.setOps(drawer.getOps());
                    return previewDialog;
                }, Platform::runLater);
    }

}
