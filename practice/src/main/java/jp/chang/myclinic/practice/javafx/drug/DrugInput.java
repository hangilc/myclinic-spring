package jp.chang.myclinic.practice.javafx.drug;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.practice.lib.RadioButtonGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DrugInput extends GridPane {

    private Label amountLabel;
    private Label daysLabel;
    private Label daysUnit;
    private List<Node> daysList = new ArrayList<>();
    private StringProperty drugName;
    private StringProperty amount;
    private StringProperty amountUnit;
    private StringProperty usage;
    private StringProperty days;
    private ObjectProperty<DrugCategory> category;
    private StringProperty comment;

    public DrugInput(){
        getStyleClass().add("drug-input");
        setupName();
        setupAmount();
        setupUsage();
        setupDays();
        setupComment();
        setupCategory();
    }

    public String getDrugName() {
        return drugName.get();
    }

    public StringProperty drugNameProperty() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName.set(drugName);
    }

    public DrugCategory getCategory() {
        return category.get();
    }

    public ObjectProperty<DrugCategory> categoryProperty() {
        return category;
    }

    public void setCategory(DrugCategory category) {
        this.category.set(category);
    }

    public String getAmount() {
        return amount.get();
    }

    public StringProperty amountProperty() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount.set(amount);
    }

    public String getAmountUnit() {
        return amountUnit.get();
    }

    public StringProperty amountUnitProperty() {
        return amountUnit;
    }

    public void setAmountUnit(String amountUnit) {
        this.amountUnit.set(amountUnit);
    }

    public String getUsage() {
        return usage.get();
    }

    public StringProperty usageProperty() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage.set(usage);
    }

    public String getDays() {
        return days.get();
    }

    public StringProperty daysProperty() {
        return days;
    }

    public void setDays(String days) {
        this.days.set(days);
    }

    public String getComment() {
        return comment.get();
    }

    public StringProperty commentProperty() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment.set(comment);
    }

    private void setupName(){
        add(new Label("名称："), 0, 0);
        Text drugNameLabel = new Text("");
        TextFlow wrapper = new TextFlow();
        wrapper.getChildren().add(drugNameLabel);
        add(wrapper, 1, 0);
        drugName = drugNameLabel.textProperty();
    }

    private void setupAmount(){
        amountLabel = new Label("用量：");
        TextField amountInput = new TextField();
        amountInput.getStyleClass().add("amount-input");
        Label amountUnitInput = new Label("");
        add(amountLabel, 0, 1);
        HBox hbox = new HBox(4);
        hbox.getChildren().addAll(amountInput, amountUnitInput);
        add(hbox, 1, 1);
        amount = amountInput.textProperty();
        amountUnit = amountUnitInput.textProperty();
    }

    private void setupUsage(){
        add(new Label("用法："), 0, 2);
        TextField usageInput = new TextField();
        Hyperlink exampleLink = new Hyperlink("例");
        HBox hbox = new HBox(4);
        hbox.getChildren().addAll(usageInput, exampleLink);
        exampleLink.setOnMousePressed(event -> doExample(event, exampleLink));
        add(hbox, 1, 2);
        usage = usageInput.textProperty();
    }

    private void doExample(MouseEvent event, Node anchor){
        ContextMenu contextMenu = new ContextMenu();
        List<String> examples = Arrays.asList(
                "分１　朝食後",
                "分２　朝夕食後",
                "分３　毎食後",
                "分１　寝る前"
        );
        examples.forEach(ex -> {
            MenuItem item = new MenuItem(ex);
            item.setOnAction(ev -> setUsage(item.getText()));
            contextMenu.getItems().add(item);
        });
        contextMenu.show(anchor, event.getScreenX(), event.getScreenY());
    }

    private void setupDays(){
        daysLabel = new Label("日数：");
        add(daysLabel, 0, 3);
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_LEFT);
        setupDaysInputArea(hbox.getChildren());
        daysList.add(daysLabel);
        daysList.add(hbox);
        add(hbox, 1, 3);
    }

    protected void setupDaysInputArea(ObservableList<Node> children){
        TextField daysInput = new TextField();
        daysInput.getStyleClass().add("days-input");
        daysUnit = new Label("日分");
        children.addAll(daysInput, daysUnit);
        days = daysInput.textProperty();
    }

    private void setupComment(){
        Text commentText = new Text("");
        TextFlow wrapper = new TextFlow();
        wrapper.getChildren().add(commentText);
        comment = commentText.textProperty();
        add(new Label("注釈："), 0, 4);
        add(wrapper, 1, 4);
    }

    private void setupCategory(){
        HBox hbox = new HBox(4);
        RadioButtonGroup<DrugCategory> categoryButtons = new RadioButtonGroup<>();
        categoryButtons.createRadioButton("内服", DrugCategory.Naifuku);
        categoryButtons.createRadioButton("屯服", DrugCategory.Tonpuku);
        categoryButtons.createRadioButton("外用", DrugCategory.Gaiyou);
        hbox.getChildren().addAll(categoryButtons.getButtons());
        category = categoryButtons.valueProperty();
        category.addListener((obs, oldValue, newValue) -> adaptToCategory());
        add(hbox, 0, 5, 2, 1);
    }

    private void adaptToCategory(){
        DrugCategory cat = category.getValue();
        if( cat != null ){
            switch(cat){
                case Naifuku: {
                    amountLabel.setText("用量：");
                    daysLabel.setText("日数：");
                    daysUnit.setText("日分");
                    setDaysVisible(true);
                    break;
                }
                case Tonpuku: {
                    amountLabel.setText("一回：");
                    daysLabel.setText("回数：");
                    daysUnit.setText("回分");
                    setDaysVisible(true);
                    break;
                }
                case Gaiyou: {
                    amountLabel.setText("用量：");
                    setDaysVisible(false);
                    break;
                }
            }
        }
    }

    private void setDaysVisible(boolean visible){
        daysList.forEach(n -> n.setVisible(visible));
    }

}
