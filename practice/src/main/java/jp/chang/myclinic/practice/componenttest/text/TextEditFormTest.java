package jp.chang.myclinic.practice.componenttest.text;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.frontend.FrontendAdapter;
import jp.chang.myclinic.mockdata.MockData;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.componenttest.CompTest;
import jp.chang.myclinic.practice.componenttest.ComponentTestBase;
import jp.chang.myclinic.practice.javafx.parts.drawerpreview.DrawerPreviewDialog;
import jp.chang.myclinic.practice.javafx.shohousen.ShohousenPreview;
import jp.chang.myclinic.practice.javafx.text.TextEditForm;
import jp.chang.myclinic.practice.javafx.text.TextEnterForm;
import jp.chang.myclinic.support.clinicinfo.ClinicInfoFileProvider;
import jp.chang.myclinic.support.config.ConfigTemp;
import jp.chang.myclinic.utilfx.ConfirmDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

public class TextEditFormTest extends ComponentTestBase {

    public TextEditFormTest(Stage stage, Pane main) {
        super(stage, main);
    }

    private TextEditForm prepareForm(TextDTO text) {
        TextEditForm form = new TextEditForm(text);
        gui(() -> {
            form.setPrefWidth(329);
            form.setPrefHeight(300);
            main.getChildren().setAll(form);
            stage.sizeToScene();
        });
        return form;
    }

    @CompTest(excludeFromBatch = true)
    public void testTextEditFormDisp() {
        TextDTO text = new TextDTO();
        text.visitId = 1;
        text.content = "体調いい。";
        prepareForm(text);
    }

    @CompTest
    public void testTextEditFormEnter() {
        TextDTO text = new TextDTO();
        text.visitId = 1;
        text.content = "体調いい。";
        String modifiedContent = text.content + "\nたちくらみもない。";
        class Local {
            private boolean confirmUpdateText;
            private boolean confirmCallback;
        }
        Local local = new Local();
        Context.frontend = new FrontendAdapter() {
            @Override
            public CompletableFuture<Void> updateText(TextDTO modified) {
                local.confirmUpdateText = modified.visitId == text.visitId &&
                        modified.content.equals(modifiedContent);
                return CompletableFuture.completedFuture(null);
            }
        };
        TextEditForm form = prepareForm(text);
        form.setOnUpdated(updated -> {
            local.confirmCallback = updated.visitId == text.visitId &&
                    updated.content.equals(modifiedContent);
        });
        gui(() -> {
            form.simulateSetText(modifiedContent);
            form.simulateClickEnterButton();
        });
        waitForTrue(8, () -> local.confirmUpdateText && local.confirmCallback);
    }

    @CompTest
    public void testTextEditFormCancel() {
        TextDTO text = new TextDTO();
        text.visitId = 1;
        text.textId = 100;
        text.content = "キャンセルテスト";
        class Local {
            private boolean confirmCallback;
        }
        Local local = new Local();
        TextEditForm form = prepareForm(text);
        form.setOnCancel(() -> {
            local.confirmCallback = true;
        });
        gui(form::simulateClickCancelButton);
        waitForTrue(10, () -> local.confirmCallback);
    }

    @CompTest
    public void testTextEditFormDelete() {
        TextDTO text = new TextDTO();
        text.visitId = 1;
        text.textId = 100;
        text.content = "削除テスト";
        class Local {
            private boolean confirmDeleteText;
            private boolean confirmCallback;
        }
        Local local = new Local();
        Context.frontend = new FrontendAdapter() {
            @Override
            public CompletableFuture<Void> deleteText(int textId) {
                confirm(textId == text.textId);
                local.confirmDeleteText = true;
                return CompletableFuture.completedFuture(null);
            }
        };
        TextEditForm form = prepareForm(text);
        form.setOnDeleted(() -> {
            local.confirmCallback = true;
        });
        gui(form::simulateClickDeleteButton);
        ConfirmDialog confirmDialog = waitForWindow(ConfirmDialog.class);
        gui(confirmDialog::simulateClickOkButton);
        waitForTrue(10, () -> local.confirmDeleteText);
        waitForTrue(10, () -> local.confirmCallback);
    }

    @CompTest
    public void testTextEditFormShohousen() {
        MockData mock = new MockData();
        PatientDTO patient = mock.pickPatientWithPatientId();
        VisitDTO visit = mock.pickVisitWithVisitId(patient.patientId, LocalDateTime.now());
        TextDTO text = new TextDTO();
        text.textId = 1;
        text.visitId = visit.visitId;
        text.content = "テスト";
        Context.frontend = new FrontendAdapter() {
            @Override
            public CompletableFuture<PatientDTO> getPatient(int patientId) {
                confirm(patientId == patient.patientId);
                return value(patient);
            }

            @Override
            public CompletableFuture<VisitDTO> getVisit(int visitId) {
                confirm(visitId == visit.visitId);
                return value(visit);
            }

            @Override
            public CompletableFuture<HokenDTO> getHoken(int visitId) {
                confirm(visitId == visit.visitId);
                HokenDTO hoken = new HokenDTO();
                return value(hoken);
            }

            @Override
            public CompletableFuture<ClinicInfoDTO> getClinicInfo() {
                ClinicInfoDTO info =
                        new ClinicInfoFileProvider(Paths.get("config/clinic-info.yml")).getClinicInfo();
                return value(info);
            }
        };
        Context.configService = new ConfigTemp();
        TextEditForm form = prepareForm(text);
        gui(form::simulateClickShohousenButton);
        ConfirmDialog confirmDialog = waitForWindow(ConfirmDialog.class);
        gui(confirmDialog::simulateClickOkButton);
        waitForWindow(DrawerPreviewDialog.class);
    }
}
