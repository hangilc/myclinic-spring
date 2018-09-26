package jp.chang.myclinic.practice.javafx.drug;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.VisitDTO;

public class EditForm extends VBox {

    //private static Logger logger = LoggerFactory.getLogger(EditForm.class);
    private DrugEditInput input = new DrugEditInput();

    public EditForm(DrugFullDTO drug, String drugTekiyou, VisitDTO visit) {
        super(4);
        getStyleClass().add("drug-form");
        getStyleClass().add("form");
        input.setDrug(drug);
        SearchTextInput searchTextInput = new SearchTextInput();
        searchTextInput.setHandler(this::onSearch);
//        super(visit, "処方の編集");
//        DrugData data = DrugData.fromDrug(drug);
//        getInput().setData(data);
//        tekiyouLabel.setText(drugTekiyou);
//        this.tekiyouRow = getInput().addRowBeforeCategory(new Label("摘要："), tekiyouLabel);
//        updateTekiyouVisibility();
//        Hyperlink deleteLink = new Hyperlink("削除");
//        Hyperlink auxLink = new Hyperlink("他");
//        deleteLink.setOnAction(evt -> doDelete());
//        auxLink.setOnMousePressed(evt -> {
//            createAuxContextMenu().show(auxLink, evt.getScreenX(), evt.getScreenY());
//        });
//        adaptTekiyouCommand();
//        tekiyouCommandBox.setAlignment(Pos.CENTER_LEFT);
//        tekiyouCommandBox.setPadding(Insets.EMPTY);
//        addToCommandBox(tekiyouCommandBox);
//        addToCommandBox(deleteLink);
//        addToCommandBox(auxLink);
//        getInput().addRow(allFixedCheck);
        getChildren().addAll(
                createTitle("処方の編集"),
                input,
                searchTextInput
        );
    }

    private void onSearch(String searchText){

    }

    protected void onUpdated(DrugFullDTO updated) {
    }

    protected void onClose() {
    }

    protected void onTekiyouModified(String newTekiyou) {
    }

    private Node createTitle(String text) {
        Label title = new Label(text);
        title.setMaxWidth(Double.MAX_VALUE);
        title.getStyleClass().add("title");
        return title;
    }


//    private ContextMenu createAuxContextMenu(){
//        ContextMenu menu = new ContextMenu();
//        {
//            MenuItem item = new MenuItem("処方例に追加");
//            item.setOnAction(evt -> doAddToPrescExample());
//            menu.getItems().add(item);
//        }
//        return menu;
//    }
//
//    private void doAddToPrescExample(){
//        Service.api.getDrugFull(getInput().getDrugId())
//                .thenAcceptAsync(drugFull -> {
//                    ConvertToPrescExampleDialog dialog = new ConvertToPrescExampleDialog(drugFull);
//                    dialog.initOwner(getScene().getWindow());
//                    dialog.initModality(Modality.WINDOW_MODAL);
//                    dialog.show();
//                }, Platform::runLater)
//                .exceptionally(HandlerFX::exceptionally);
//    }
//
//    private void adaptTekiyouCommand(){
//        String drugTekiyou = getTekiyou();
//        if (drugTekiyou == null || drugTekiyou.isEmpty()) {
//            Hyperlink tekiyouLink = new Hyperlink("摘要入力");
//            tekiyouLink.setOnAction(evt -> doEnterTekiyou());
//            tekiyouCommandBox.getChildren().setAll(tekiyouLink);
//        } else {
//            Hyperlink editTekiyouLink = new Hyperlink("摘要編集");
//            Hyperlink deleteTekiyouLink = new Hyperlink("摘要削除");
//            editTekiyouLink.setOnAction(evt -> doEnterTekiyou());
//            deleteTekiyouLink.setOnAction(evt -> doDeleteTekiyou());
//            tekiyouCommandBox.getChildren().setAll(editTekiyouLink, deleteTekiyouLink);
//        }
//
//    }
//
//    private boolean hasTekiyou() {
//        String tekiyou = getTekiyou();
//        return tekiyou != null && !tekiyou.isEmpty();
//    }
//
//    private void updateTekiyouVisibility() {
//        boolean visible = hasTekiyou();
//        tekiyouRow.setManaged(visible);
//        tekiyouRow.setVisible(visible);
//    }
//
//    @Override
//    void doEnter() {
//        DrugDTO drug = getInput().createDrug(getVisitId(), 0);
//        if( drug.drugId == 0 ){
//            throw new RuntimeException("drugId is null.");
//        }
//        Service.api.updateDrug(drug)
//                .thenCompose(ok -> Service.api.getDrugFull(getInput().getDrugId()))
//                .thenAcceptAsync(this::onUpdated, Platform::runLater)
//                .exceptionally(HandlerFX::exceptionally);
//    }
//
//    private void doDelete() {
//        if (GuiUtil.confirm("この処方を削除していいですか？")) {
//            class Local {
//                private DrugDTO drug;
//            }
//            Local local = new Local();
//            Service.api.getDrug(getInput().getDrugId())
//                    .thenCompose(drugDTO -> {
//                        local.drug = drugDTO;
//                        return Service.api.deleteDrug(drugDTO.drugId);
//                    })
//                    .thenAccept(ok -> {
//                        DrugDeletedEvent event = new DrugDeletedEvent(local.drug);
//                        Platform.runLater(() -> EditForm.this.fireEvent(event));
//                    })
//                    .exceptionally(HandlerFX::exceptionally);
//        }
//    }
//
//    private String getTekiyou() {
//        return tekiyouLabel.getText();
//    }
//
//    private void setTekiyou(String text) {
//        tekiyouLabel.setText(text);
//    }
//
//    private void doEnterTekiyou() {
//        String curr = getTekiyou();
//        if (curr == null) {
//            curr = "";
//        }
//        GuiUtil.askForString("摘要の内容", curr).ifPresent(str -> {
//            Service.api.setDrugTekiyou(getInput().getDrugId(), str)
//                    .thenAccept(ok -> {
//                        Platform.runLater(() -> {
//                            setTekiyou(str);
//                            updateTekiyouVisibility();
//                            adaptTekiyouCommand();
//                            onTekiyouModified(str);
//                        });
//                    })
//                    .exceptionally(HandlerFX::exceptionally);
//        });
//    }
//
//    private void doDeleteTekiyou() {
//        if (GuiUtil.confirm("現在の摘要を削除しますか？")) {
//            Service.api.deleteDrugTekiyou(getInput().getDrugId())
//                    .thenAccept(ok -> {
//                        Platform.runLater(() -> {
//                            setTekiyou(null);
//                            updateTekiyouVisibility();
//                            adaptTekiyouCommand();
//                            onTekiyouModified(null);
//                        });
//                    })
//                    .exceptionally(HandlerFX::exceptionally);
//        }
//    }
//
//    @Override
//    Set<Input.SetOption> getSetOptions() {
//        Set<Input.SetOption> opts = new HashSet<>();
//        if (allFixedCheck.isSelected()) {
//            opts.add(Input.SetOption.MasterOnly);
//        }
//        return opts;
//    }
//
//    protected void onUpdated(DrugFullDTO updated) {
//    }
//
//    protected void onClose() {
//    }
//
//    protected void onTekiyouModified(String newTekiyou) {
//    }
}
