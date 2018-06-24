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
import jp.chang.myclinic.dto.MeisaiDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.PaymentDTO;
import jp.chang.myclinic.drawer.printer.PrinterEnv;
import jp.chang.myclinic.reception.ReceptionEnv;
import jp.chang.myclinic.reception.drawerpreviewfx.DrawerPreviewStage;
import jp.chang.myclinic.reception.lib.ReceptionService;
import jp.chang.myclinic.reception.receipt.ReceiptDrawer;
import jp.chang.myclinic.reception.receipt.ReceiptDrawerData;
import jp.chang.myclinic.reception.receipt.ReceiptDrawerDataCreator;
import jp.chang.myclinic.reception.tracker.DispatchHook;
import jp.chang.myclinic.reception.tracker.model.Wqueue;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class MainPane extends VBox implements DispatchHook {
    private static Logger logger = LoggerFactory.getLogger(MainPane.class);

    private Button searchPatientButton = new Button("患者検索");
    private Button searchPaymentButton = new Button("会計検索");

    private TextField patientIdField = new TextField();

    private WqueueTable wqueueTable = new WqueueTable();
    private ObservableList<Wqueue> wqueueList = FXCollections.observableArrayList(wq -> new Observable[]{
            wq.waitStateProperty()
    });

    public MainPane() {
        setSpacing(4);
        {
            HBox hbox = new HBox(4);
            hbox.setAlignment(Pos.CENTER_LEFT);
            Button newPatientButton = new Button("新規患者");
            Button blankReceiptButton = new Button("領収書用紙");
            newPatientButton.setOnAction(event -> doNewPatient());
            searchPatientButton.setOnAction(event -> doSearchPatient());
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
            Button refreshButton = new Button("更新");
            Button cashierButton = new Button("会計");
            Button deselectButton = new Button("選択解除");
            Button deleteButton = new Button("削除");
            refreshButton.setOnAction(event -> doRefresh());
            cashierButton.setOnAction(event -> doCashier());
            deselectButton.setOnAction(event -> doDeselect());
            deleteButton.setOnAction(event -> doDelete());
            hbox.getChildren().addAll(refreshButton, cashierButton, deselectButton, deleteButton);
            getChildren().add(hbox);
        }
    }

    public void setWqueueList(ObservableList<Wqueue> wqueueList){
        ObservableList<Wqueue> list = FXCollections.observableList(wqueueList, wq -> {
            return new Observable[]{
                wq.waitStateProperty()
            };
        });
        wqueueTable.setItems(list);
    }

    private void doCashier() {
        wqueueTable.getSelectedWqueueFullDTO()
                .thenAccept(wq -> {
                    class Store {
                        MeisaiDTO meisai;
                        List<PaymentDTO> payments;
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
        wqueueTable.getSelectedWqueueFullDTO()
                .thenAccept(wq -> {
                    if (wq != null) {
                        String message = String.format("この診察受付（%s%s）を削除していいですか？", wq.patient.lastName, wq.patient.firstName);
                        if (GuiUtil.confirm(message)) {
                            ReceptionService.deleteFromWqueue(wq.visit.visitId);
                        }
                    }
                })
                .exceptionally(HandlerFX::exceptionally);
    }

    private void doNewPatient() {
        EditPatientStage stage = new EditPatientStage(null);
        stage.setTitle("新規患者入力");
        stage.showAndWait();
        PatientDTO formValue = stage.getFormValue();
        if (formValue != null) {
            Service.api.enterPatient(formValue)
                    .thenCompose(patientId -> {
                        formValue.patientId = patientId;
                        return Service.api.listHoken(patientId);
                    })
                    .thenAccept(hokenList -> {
                        Platform.runLater(() -> {
                            PatientWithHokenStage patientWithHokenStage = new PatientWithHokenStage(formValue, hokenList);
                            patientWithHokenStage.showAndWait();
                        });
                    })
                    .exceptionally(ex -> {
                        logger.error("List hoken failed.", ex);
                        Platform.runLater(() -> GuiUtil.alertException("保険情報が取得できませんでした。", ex));
                        return null;
                    });
        }
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
        creator.setClinicInfo(ReceptionEnv.INSTANCE.getClinicInfo());
        ReceiptDrawerData data = creator.getData();
        ReceiptDrawer receiptDrawer = new ReceiptDrawer(data);
        final List<Op> ops = receiptDrawer.getOps();
        PrinterEnv printerEnv = null;
        try {
            printerEnv = ReceptionEnv.INSTANCE.getMyclinicEnv().getPrinterEnv();
        } catch (Exception ex) {
            logger.error("Failed to get PrinterEnv", ex);
            GuiUtil.alertError("Failed to get PrinterEnv");
        }
        DrawerPreviewStage stage = new DrawerPreviewStage(ops, PaperSize.A6_Landscape,
                printerEnv, "reception-receipt");
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
//            Service.api.startVisit(patient.patientId)
//                    .thenAccept(visitId -> {
//                        // TODO: update wqueue table
//                    })
//                    .exceptionally(ex -> {
//                        logger.error("Failed start visit.", ex);
//                        Platform.runLater(() -> GuiUtil.alertException(ex));
//                        return null;
//                    });
        }
    }

    private void doRefresh() {
        //ReceptionEnv.INSTANCE.getWqueueReloader().trigger();
    }

    @Override
    public void onWqueueCreated(Wqueue created, Runnable toNext) {
        wqueueList.add(created);
        toNext.run();
    }

    @Override
    public void onWqueueDeleted(int visitId, Runnable toNext) {
        wqueueList.removeIf(wq -> wq.getVisit().getVisitId() == visitId);
        toNext.run();
    }
}
