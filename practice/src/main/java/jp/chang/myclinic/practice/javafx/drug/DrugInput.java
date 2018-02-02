package jp.chang.myclinic.practice.javafx.drug;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.practice.lib.RadioButtonGroup;

import java.util.ArrayList;
import java.util.List;

public class DrugInput extends GridPane {

    private Text drugNameLabel;
    private Label amountLabel;
    private TextField amountInput;
    private Label amountUnit;
    private TextField usageInput;
    private Label daysLabel;
    private TextField daysInput;
    private Label daysUnit;
    private List<? extends Node> daysList = new ArrayList<>();
    private RadioButtonGroup<DrugCategory> categoryButtons;
    private StringProperty drugName;
    private ObjectProperty<DrugCategory> category;

    public DrugInput(){
        getStyleClass().add("drug-input");
        setupName();
        setupAmount();
        setupUsage();
        setupDays();
        setupCategory();
        drugName.setValue("アムロジピン");
        category.setValue(DrugCategory.Naifuku);
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

    private void setupName(){
        add(new Label("名称"), 0, 0);
        drugNameLabel = new Text("");
        TextFlow wrapper = new TextFlow();
        wrapper.getChildren().add(drugNameLabel);
        add(wrapper, 1, 0);
        drugName = drugNameLabel.textProperty();
    }

    private void setupAmount(){
        amountLabel = new Label("容量");
        amountInput = new TextField();
        amountInput.getStyleClass().add("amount-input");
        amountUnit = new Label("");
        add(amountLabel, 0, 1);
        HBox hbox = new HBox(4);
        hbox.getChildren().addAll(amountInput, amountUnit);
        add(hbox, 1, 1);
    }

    private void setupUsage(){
        add(new Label("用法"), 0, 2);
        usageInput = new TextField();
        Hyperlink exampleLink = new Hyperlink("例");
        HBox hbox = new HBox(4);
        hbox.getChildren().addAll(usageInput, exampleLink);
        add(hbox, 1, 2);
    }

    private void setupDays(){
        daysLabel = new Label("日数");
        add(daysLabel, 0, 3);
        daysInput = new TextField();
        daysInput.getStyleClass().add("days-input");
        daysUnit = new Label("日分");
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().addAll(daysInput, daysUnit);
        add(hbox, 1, 3);
        daysList.addAll(daysLabel, hbox);
    }

    private void setupCategory(){
        HBox hbox = new HBox(4);
        categoryButtons = new RadioButtonGroup<>();
        categoryButtons.createRadioButton("内服", DrugCategory.Naifuku);
        categoryButtons.createRadioButton("屯服", DrugCategory.Tonpuku);
        categoryButtons.createRadioButton("外用", DrugCategory.Gaiyou);
        hbox.getChildren().addAll(categoryButtons.getButtons());
        category = categoryButtons.valueProperty();
        category.addListener((obs, oldValue, newValue) -> adaptToCategory());
        add(hbox, 0, 4, 2, 1);
    }

    private void adaptToCategory(){
        DrugCategory cat = category.getValue();
        if( cat != null ){
            switch(cat){
                case Naifuku: {
                    amountLabel.setText("用量");
                    daysLabel.setText("日数");
                    daysUnit.setText("日分");
                    setDaysVisible(true);
                    break;
                }
                case Tonpuku: {
                    amountLabel.setText("一回");
                    daysLabel.setText("回数");
                    daysUnit.setText("回分");
                    setDaysVisible(true);
                    break;
                }
                case Gaiyou: {
                    amountLabel.setText("用量");
                    setDaysVisible(false);
                    break;
                }
            }
        }
    }

    private void setDaysVisible(boolean visible){

    }

}
