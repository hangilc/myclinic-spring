package jp.chang.myclinic.practice.javafx.drug;

import javafx.beans.property.ObjectProperty;
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
import jp.chang.myclinic.practice.lib.drug.DrugFormGetter;
import jp.chang.myclinic.practice.lib.drug.DrugFormSetter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class DrugInput extends GridPane implements DrugFormGetter, DrugFormSetter {

    private int iyakuhincode = 0;
    private Text drugNameLabel = new Text("");
    private Label amountLabel;
    private TextField amountInput = new TextField();
    private Label amountUnitLabel = new Label("");
    private TextField usageInput = new TextField();
    private HBox daysRow;
    private Label daysLabel;
    private TextField daysInput = new TextField();
    private Label daysUnit;
    private List<Node> daysList = new ArrayList<>();
    private ObjectProperty<DrugCategory> category;
    private Text commentText = new Text("");
    private int row = 0;

    DrugInput() {
        getStyleClass().add("drug-input");
        setupName();
        setupAmount();
        setupUsage();
        setupDays();
        setupComment();
        setupCategory();
    }

    void addToDaysRow(Node ...nodes){
        daysRow.getChildren().addAll(nodes);
    }

    @Override
    public int getIyakuhincode() {
        return iyakuhincode;
    }

    @Override
    public void setIyakuhincode(int iyakuhincode) {
        this.iyakuhincode = iyakuhincode;
    }

    @Override
    public String getAmount() {
        return amountInput.getText();
    }

    @Override
    public String getUsage() {
        return usageInput.getText();
    }

    @Override
    public String getDays() {
        return daysInput.getText();
    }

    @Override
    public DrugCategory getCategory() {
        return category.getValue();
    }

    @Override
    public void setDrugName(String name) {
        drugNameLabel.setText(name);
    }

    @Override
    public void setAmount(String value) {
        amountInput.setText(value);
    }

    @Override
    public void setAmountUnit(String value) {
        amountUnitLabel.setText(value);
    }

    @Override
    public void setUsage(String value) {
        usageInput.setText(value);
    }

    @Override
    public void setDays(String value) {
        daysInput.setText(value);
    }

    @Override
    public void setCategory(DrugCategory category) {
        this.category.setValue(category);
    }

    @Override
    public void setComment(String comment) {
        commentText.setText(comment);
    }

    public void addRow(Node node){
        add(node, 0, row, 2, 1);
    }

    private void setupName() {
        add(new Label("名称："), 0, row);
        TextFlow wrapper = new TextFlow();
        wrapper.getChildren().add(drugNameLabel);
        add(wrapper, 1, row);
        row += 1;
    }

    private void setupAmount() {
        amountLabel = new Label("用量：");
        amountInput.getStyleClass().add("amount-input");
        add(amountLabel, 0, row);
        HBox hbox = new HBox(4);
        hbox.getChildren().addAll(amountInput, amountUnitLabel);
        add(hbox, 1, row);
        row += 1;
    }

    private void setupUsage() {
        add(new Label("用法："), 0, row);
        Hyperlink exampleLink = new Hyperlink("例");
        HBox hbox = new HBox(4);
        hbox.getChildren().addAll(usageInput, exampleLink);
        exampleLink.setOnMousePressed(event -> doExample(event, exampleLink));
        add(hbox, 1, row);
        row += 1;
    }

    private void doExample(MouseEvent event, Node anchor) {
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

    private void setupDays() {
        daysLabel = new Label("日数：");
        add(daysLabel, 0, row);
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_LEFT);
        setupDaysInputArea(hbox.getChildren());
        daysList.add(daysLabel);
        daysList.add(hbox);
        add(hbox, 1, row);
        daysRow = hbox;
        row += 1;
    }

    private void setupDaysInputArea(ObservableList<Node> children) {
        daysInput.getStyleClass().add("days-input");
        daysUnit = new Label("日分");
        children.addAll(daysInput, daysUnit);
    }

    private void setupComment() {
        TextFlow wrapper = new TextFlow();
        wrapper.getChildren().add(commentText);
        add(new Label("注釈："), 0, row);
        add(wrapper, 1, row);
        row += 1;
    }

    private void setupCategory() {
        HBox hbox = new HBox(4);
        RadioButtonGroup<DrugCategory> categoryButtons = new RadioButtonGroup<>();
        categoryButtons.createRadioButton("内服", DrugCategory.Naifuku);
        categoryButtons.createRadioButton("屯服", DrugCategory.Tonpuku);
        categoryButtons.createRadioButton("外用", DrugCategory.Gaiyou);
        hbox.getChildren().addAll(categoryButtons.getButtons());
        categoryButtons.setValue(DrugCategory.Naifuku);
        category = categoryButtons.valueProperty();
        category.addListener((obs, oldValue, newValue) -> adaptToCategory());
        add(hbox, 0, row, 2, 1);
        row += 1;
    }

    private void adaptToCategory() {
        DrugCategory cat = category.getValue();
        if (cat != null) {
            switch (cat) {
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

    private void setDaysVisible(boolean visible) {
        daysList.forEach(n -> n.setVisible(visible));
    }

}
