package jp.chang.myclinic.practice.javafx.drug;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextFlow;

public class DrugInput extends GridPane {

    private TextFlow drugName;
    private Label amountLabel;
    private TextField amountInput;
    private Label amountUnit;
    private TextField usageInput;
    private Label daysLabel;
    private TextField daysInput;
    private Label daysUnit;

    public DrugInput(){
        getStyleClass().add("drug-input");
        setupName();
        setupAmount();
        setupUsage();
        setupDays();
        setupCategory();
    }

    private void setupName(){
        add(new Label("名称"), 0, 0);
        drugName = new TextFlow();
        add(drugName, 1, 0);
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
    }

    private void setupCategory(){
        HBox hbox = new HBox(4);
        RadioButton naifukuRadio = new RadioButton("内服");
        RadioButton tonpukuButton = new RadioButton("屯服");
        RadioButton gaiyouButton = new RadioButton("外用");
        ToggleGroup group = new ToggleGroup();
        RadioButton[] buttons = new RadioButton[]{ naifukuRadio, tonpukuButton, gaiyouButton };
        group.getToggles().addAll(buttons);
        hbox.getChildren().addAll(buttons);
        add(hbox, 0, 4, 2, 1);
    }

}
