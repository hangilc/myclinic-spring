package jp.chang.myclinic.practice.javafx.drug;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.lib.drug.DrugFormGetter;
import jp.chang.myclinic.practice.lib.drug.DrugFormSetter;
import jp.chang.myclinic.practice.lib.drug.DrugInputConstraints;
import jp.chang.myclinic.practice.lib.drug.DrugSearchResultModel;

public class DrugForm extends VBox implements DrugFormGetter, DrugFormSetter {

    private int patientId;
    private int drugId;
    private int visitId;
    private int iyakuhincode;
    private String at;
    private DrugInput drugInput;

    public DrugForm(VisitDTO visit){
        super(4);
        this.patientId = visit.patientId;
        this.drugId = 0;
        this.visitId = visit.visitId;
        this.at = visit.visitedAt;
        getStyleClass().add("drug-form");
        getStyleClass().add("form");
        getChildren().addAll(
                createTitle(),
                createDisp(),
                createButtons(),
                createSearch()
        );
    }

    private Node createTitle(){
        Label title = new Label("新規処方の入力");
        title.setMaxWidth(Double.MAX_VALUE);
        title.getStyleClass().add("title");
        return title;
    }

    protected DrugInputConstraints getInputConstraints() {
        return DrugInputConstraints.defaultDrugInputConstraints();
    }

    protected DrugInput createDrugInput(){
        return new DrugInput();
    }

    private Node createDisp(){
        this.drugInput = createDrugInput();
        return drugInput;
    }

    protected void onEnter(DrugForm self){

    }

    protected void onClose(DrugForm self){

    }

    private Node createButtons(){
        VBox vbox = new VBox(4);
        vbox.getStyleClass().add("commands");
        {
            HBox hbox = new HBox(4);
            Button enterButton = new Button("入力");
            Button closeButton = new Button("閉じる");
            Hyperlink clearLink = new Hyperlink("クリア");
            enterButton.setOnAction(event -> onEnter(this));
            closeButton.setOnAction(event -> onClose(this));
            clearLink.setOnAction(event -> clearForm(getInputConstraints()));
            hbox.getChildren().addAll(enterButton, closeButton, clearLink);
            vbox.getChildren().add(hbox);
        }
        return vbox;
    }

    private Node createSearch(){
        DrugSearch drugSearch = new DrugSearch(patientId, at);
        drugSearch.setCallback(new DrugSearch.Callback() {
            @Override
            public void onSelect(DrugSearchResultModel searchResultModel) {
                searchResultModel.stuffInto(DrugForm.this, DrugForm.this, getInputConstraints());
            }
        });
        return drugSearch;
    }

    @Override
    public int getDrugId() {
        return drugId;
    }

    @Override
    public int getVisitId() {
        return visitId;
    }

    @Override
    public int getIyakuhincode() {
        return iyakuhincode;
    }

    @Override
    public String getAmount() {
        return drugInput.getAmount();
    }

    @Override
    public String getUsage() {
        return drugInput.getUsage();
    }

    @Override
    public String getDays() {
        return drugInput.getDays();
    }

    @Override
    public DrugCategory getCategory() {
        return drugInput.getCategory();
    }

    @Override
    public void setDrugId(int drugId) {
        this.drugId = drugId;
    }

    @Override
    public void setVisitId(int visitId) {
        this.visitId = visitId;
    }

    @Override
    public void setIyakuhincode(int iyakuhincode) {
        this.iyakuhincode = iyakuhincode;
    }

    @Override
    public void setDrugName(String name) {
        drugInput.setDrugName(name);
    }

    @Override
    public void setAmount(String value) {
        drugInput.setAmount(value);
    }

    @Override
    public void setAmountUnit(String value) {
        drugInput.setAmountUnit(value);
    }

    @Override
    public void setUsage(String value) {
        drugInput.setUsage(value);
    }

    @Override
    public void setDays(String value) {
        drugInput.setDays(value);
    }

    @Override
    public void setCategory(DrugCategory category) {
        drugInput.setCategory(category);
    }

    @Override
    public void setComment(String comment) {
        drugInput.setComment(comment);
    }
}
