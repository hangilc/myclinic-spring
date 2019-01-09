package jp.chang.myclinic.reception.javafx;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.consts.WqueueWaitState;
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.PaperSize;
import jp.chang.myclinic.drawer.printer.PrinterEnv;
import jp.chang.myclinic.dto.MeisaiDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.PaymentDTO;
import jp.chang.myclinic.reception.Globals;
import jp.chang.myclinic.reception.drawerpreviewfx.DrawerPreviewStage;
import jp.chang.myclinic.reception.event.RefreshEvent;
import jp.chang.myclinic.reception.javafx.edit_patient.EnterPatientStage;
import jp.chang.myclinic.reception.receipt.ReceiptDrawer;
import jp.chang.myclinic.reception.receipt.ReceiptDrawerData;
import jp.chang.myclinic.reception.receipt.ReceiptDrawerDataCreator;
import jp.chang.myclinic.reception.tracker.DispatchHook;
import jp.chang.myclinic.reception.tracker.model.Wqueue;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MainPane extends VBox implements DispatchHook {
    private static Logger logger = LoggerFactory.getLogger(MainPane.class);

    private TextField patientIdField = new TextField();
    private WqueueTable wqueueTable = new WqueueTable();
    private ObservableList<WqueueTable.Model> wqueueList = FXCollections.observableArrayList(wq -> new Observable[]{
            wq.waitStateProperty(),
            wq.lastNameProperty(),
            wq.firstNameProperty(),
            wq.lastNameYomiProperty(),
            wq.firstNameYomiProperty(),
            wq.sexProperty(),
            wq.birthdayProperty()
    });

    public MainPane() {
        setSpacing(4);
        {
            HBox hbox = new HBox(4);
            hbox.setAlignment(Pos.CENTER_LEFT);
            Button newPatientButton = new Button("新規患者");
            Button blankReceiptButton = new Button("領収書用紙");
            newPatientButton.setOnAction(event -> doNewPatient());
            Button searchPatientButton = new Button("患者検索");
            searchPatientButton.setOnAction(event -> doSearchPatient());
            Button searchPaymentButton = new Button("会計検索");
            searchPaymentButton.setOnAction(event -> doSearchPayment());
            blankReceiptButton.setOnAction(event -> doBlankReceipt());
            hbox.getChildren().addAll(newPatientButton, searchPatientButton, searchPaymentButton, blankReceiptButton);
            getChildren().add(hbox);
        }
        {
            HBox hbox = new HBox(4);
            hbox.setAlignment(Pos.CENTER_LEFT);
            patientIdField.setPrefWidth(60);
            patientIdField.setMaxWidth(Control.USE_PREF_SIZE);
            patientIdField.setMinWidth(Control.USE_PREF_SIZE);
            patientIdField.setOnAction(event -> doRegisterForPractice());
            Button registerForPracticeButton = new Button("診療受付");
            Button patientInfoButton = new Button("患者情報");
            registerForPracticeButton.setOnAction(event -> doRegisterForPractice());
            patientInfoButton.setOnAction(event -> doPatientInfo());
            hbox.getChildren().addAll(new Label("患者番号"), patientIdField, registerForPracticeButton, patientInfoButton);
            getChildren().add(hbox);
        }
        {
            wqueueTable.itemsProperty().set(wqueueList);
            getChildren().add(wqueueTable);
        }
        {
            HBox hbox = new HBox(4);
            hbox.setAlignment(Pos.CENTER_LEFT);
            Button refreshButton = new Button("更新");
            Button cashierButton = new Button("会計");
            Button deselectButton = new Button("選択解除");
            Button deleteButton = new Button("削除");
            Label noSyncNotice = new Label("非同期中");
            noSyncNotice.getStyleClass().add("no-sync-notice");
            refreshButton.setOnAction(event -> doRefresh());
            cashierButton.setOnAction(event -> doCashier());
            deselectButton.setOnAction(event -> doDeselect());
            deleteButton.setOnAction(event -> doDelete());
            Runnable noSyncNoticeUpdater = () -> {
                boolean visible = !Globals.isTracking();
                noSyncNotice.setVisible(visible);
                noSyncNotice.setManaged(visible);
            };
            noSyncNoticeUpdater.run();
            Globals.trackingProperty().addListener((obs, oldValue, newValue) -> noSyncNoticeUpdater.run());
            hbox.getChildren().addAll(refreshButton, cashierButton, deselectButton, deleteButton,
                    noSyncNotice);
            getChildren().add(hbox);
        }
    }

    private void doCashier() {
        wqueueTable.getSelectedWqueueFullDTO()
                .thenAccept(wq -> {
                    class Store {
                        private MeisaiDTO meisai;
                        private List<PaymentDTO> payments;
                    }
                    if (wq != null) {
                        WqueueWaitState state = WqueueWaitState.fromCode(wq.wqueue.waitState);
                        if (state == WqueueWaitState.WaitCashier) {
                            int visitId = wq.visit.visitId;
                            Store store = new Store();
                            Service.api.getVisitMeisai(visitId)
                                    .thenCompose(meisai -> {
                                        store.meisai = meisai;
                                        return Service.api.listPayment(visitId);
                                    })
                                    .thenCompose(payments -> {
                                        store.payments = payments;
                                        return Service.api.getCharge(visitId);
                                    })
                                    .thenAccept(charge -> Platform.runLater(() -> {
                                        CashierDialog cashierDialog = new CashierDialog(store.meisai, wq.patient,
                                                store.payments, wq.visit, charge);
                                        cashierDialog.show();
                                    }))
                                    .exceptionally(HandlerFX::exceptionally);
                        }
                    }
                })
                .exceptionally(HandlerFX::exceptionally);
    }

    private void doDeselect() {
        wqueueTable.getSelectionModel().select(null);
    }

    private void doDelete() {
        int visitId = wqueueTable.getVisitIdOfSelectedWqueue();
        if (visitId > 0) {
            String message = "この診察受付を削除していいですか？";
            if (GuiUtil.confirm(message)) {
                ReceptionService.deleteFromWqueue(visitId);
            }
        }
    }

    private void doNewPatient() {
        MainPane mainPane = this;
        EnterPatientStage stage = new EnterPatientStage();
        stage.setOnEnterCallback(dto -> {
            Service.api.enterPatient(dto)
                    .thenCompose(patientId -> {
                        dto.patientId = patientId;
                        return Service.api.listHoken(patientId);
                    })
                    .thenAcceptAsync(hokenList -> {
                        PatientWithHokenStage patientWithHokenStage = new PatientWithHokenStage(dto, hokenList);
                        patientWithHokenStage.initOwner(mainPane.getScene().getWindow());
                        stage.close();
                        patientWithHokenStage.showAndWait();
                    }, Platform::runLater)
                    .exceptionally(ex -> {
                        logger.error("List hoken failed.", ex);
                        Platform.runLater(() -> GuiUtil.alertException("保険情報が取得できませんでした。", ex));
                        return null;
                    });
        });
        stage.show();
    }

    private void doSearchPatient() {
        SearchPatientStage stage = new SearchPatientStage();
        stage.show();
    }

    private void doSearchPayment() {
        SearchPaymentStage stage = new SearchPaymentStage();
        stage.show();
    }

    private void doPatientInfo() {
        String text = patientIdField.getText().trim();
        if (text.isEmpty()) {
            return;
        }
        try {
            int patientId = Integer.parseInt(text);
            Service.api.getPatient(patientId)
                    .thenAccept(patient -> {
                        Platform.runLater(() -> {
                            patientIdField.setText("");
                            PatientInfoStage stage = new PatientInfoStage(patient);
                            stage.initOwner(MainPane.this.getScene().getWindow());
                            stage.show();
                        });
                    })
                    .exceptionally(ex -> {
                        logger.error("failed to fetch patient", ex);
                        Alert alert = new Alert(Alert.AlertType.ERROR, "患者情報が取得できませんでした。" + ex, ButtonType.OK);
                        alert.showAndWait();
                        return null;
                    });
        } catch (NumberFormatException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "患者番号の入力が適切でありません。", ButtonType.OK);
            alert.showAndWait();
        }
    }

    private void doBlankReceipt() {
        ReceiptDrawerDataCreator creator = new ReceiptDrawerDataCreator();
        creator.setClinicInfo(Globals.getClinicInfo());
        ReceiptDrawerData data = creator.getData();
        ReceiptDrawer receiptDrawer = new ReceiptDrawer(data);
        final List<Op> ops = receiptDrawer.getOps();
        PrinterEnv printerEnv = null;
        try {
            printerEnv = Globals.getPrinterEnv();
        } catch (Exception ex) {
            logger.error("Failed to get PrinterEnv", ex);
            GuiUtil.alertError("Failed to get PrinterEnv");
        }
        DrawerPreviewStage stage = new DrawerPreviewStage(ops, PaperSize.A6_Landscape,
                printerEnv,
                Globals::getReceiptPrinterSetting,
                Globals::setReceiptPrinterSetting);
        stage.show();
    }

    private void doRegisterForPractice() {
        String patientIdInput = patientIdField.getText().trim();
        if (patientIdInput.isEmpty()) {
            return;
        }
        try {
            int patientId = Integer.parseInt(patientIdInput);
            Service.api.getPatient(patientId)
                    .thenAccept(patient -> {
                        Platform.runLater(() -> {
                            patientIdField.setText("");
                            registerForPractice(patient);
                        });
                    })
                    .exceptionally(ex -> {
                        logger.error("Failed get patient.", ex);
                        Platform.runLater(() -> GuiUtil.alertError("該当する患者を見つけられませんでした。"));
                        return null;
                    });
        } catch (NumberFormatException numberFormatEsception) {
            Service.api.searchPatient(patientIdInput)
                    .thenAccept(patients -> {
                        if (patients.size() == 0) {
                            Platform.runLater(() -> {
                                GuiUtil.alertError("該当する患者が見つかりませんでした。");
                            });
                        } else {
                            Platform.runLater(() -> {
                                SelectPatientDialog dialog = new SelectPatientDialog("診療受付をする患者を選択してください。", patients);
                                dialog.showAndWait();
                                PatientDTO selection = dialog.getSelection();
                                if (selection != null) {
                                    patientIdField.setText("");
                                    registerForPractice(selection);
                                }
                            });
                        }
                    })
                    .exceptionally(ex -> {
                        logger.error("Failed search patient", ex);
                        Platform.runLater(() -> GuiUtil.alertException("患者情報が取得できませんでした。", ex));
                        return null;
                    });
        } catch (Exception ex) {
            logger.error("Unexpected exception", ex);
            GuiUtil.alertException("Internal error.", ex);
        }
    }

    private void registerForPractice(PatientDTO patient) {
        RegisterForPracticeDialog dialog = new RegisterForPracticeDialog(patient);
        dialog.showAndWait();
        if (dialog.isOk()) {
            ReceptionService.startVisit(patient.patientId);
        }
    }

    private void doRefresh() {
        fireEvent(new RefreshEvent());
    }

    public void setWqueueModels(List<WqueueTable.Model> models) {
        wqueueList.setAll(models);
    }

    @Override
    public void onWqueueCreated(Wqueue created, Runnable toNext) {
        wqueueList.add(new WqueueModel(created));
        toNext.run();
    }

    @Override
    public void onWqueueDeleted(int visitId, Runnable toNext) {
        wqueueList.removeIf(wq -> wq.getVisitId() == visitId);
        wqueueTable.getSelectionModel().clearSelection();
        toNext.run();
    }
}
