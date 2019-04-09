package jp.chang.myclinic.practice.javafx.drug.lib2;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.RadioButtonGroup;

import java.text.DecimalFormat;

public class DrugEnterInput extends VBox {

    private Text drugNameLabel = new Text("");
    private Label amountLabel = new Label("");
    private TextField amountInput = new TextField();
    private Label amountUnitLabel = new Label("");
    private TextField usageInput = new TextField();
    private HBox daysRow;
    private Label daysLabel = new Label("");
    private TextField daysInput = new TextField();
    private Label daysUnit = new Label("");
    private ObjectProperty<DrugCategory> category;
    private DecimalFormat amountFormatter = new DecimalFormat("###.##");
    private Node categoryRow;
    private Hyperlink exampleLink = new Hyperlink("例");
    private String[] exampleTexts = new String[]{
            "分１　朝食後",
            "分２　朝夕食後",
            "分３　毎食後",
            "分１　寝る前"
    };
    private ContextMenu exampleContextMenu;
    private Label commentLabel = new Label();
    private HBox commentRow;
    private Label tekiyouLabel = new Label();
    private HBox tekiyouRow;
    private CheckBox daysFixedCheck = new CheckBox("固定");
    private FixedDaysService fixedDaysService = new FixedDaysService();

    public DrugEnterInput() {
        super(4);
        getStyleClass().add("drug-input");
        amountInput.getStyleClass().add("amount-input");
        daysInput.getStyleClass().add("days-input");
        addRow(new Label("名称："), new TextFlow(drugNameLabel));
        addRow(amountLabel, createAmountContent());
        addRow(new Label("用法："), createUsageContent());
        daysRow = addRow(daysLabel, createDaysContent());
        this.categoryRow = addRow(createCategoryContent());
        category.addListener((obs, oldValue, newValue) -> onCategoryChange(newValue));
        category.setValue(null);
        category.setValue(DrugCategory.Naifuku);
        addLabelContextMenu();
        this.commentRow = addRowBeforeCategory(new Label("注釈："), commentLabel);
        adaptComment();
        commentLabel.textProperty().addListener((obs, oldValue, newValue) -> adaptComment());
        this.tekiyouRow = addRowBeforeCategory(new Label("摘要："), tekiyouLabel);
        adaptTekiyou();
        tekiyouLabel.textProperty().addListener((obs, oldValue, newValue) -> adaptTekiyou());
        daysFixedCheck.setSelected(true);
        addToDaysRow(daysFixedCheck);
        daysFixedCheck.selectedProperty().addListener((obs, oldValue, newValue) ->
                adaptToDaysFixedChange(newValue)
        );
    }

    public void onDrugEntered(DrugDTO drug){
        fixedDaysService.onDaysSubmitted("" + drug.days, DrugCategory.fromCode(drug.category));
    }

    private HBox addRow(Label label, Node content) {
        return addRowBefore(label, content, null);
    }

    private HBox addRowBeforeCategory(Label label, Node content) {
        return addRowBefore(label, content, categoryRow);
    }

    private Node addRow(Node content) {
        getChildren().add(content);
        return content;
    }

    private HBox addRowBefore(Label label, Node content, Node beforeNode) {
        HBox hbox = new HBox(4);
        label.setMinWidth(Control.USE_PREF_SIZE);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().addAll(label, content);
        if (beforeNode == null) {
            getChildren().add(hbox);
        } else {
            int index = getChildren().indexOf(categoryRow);
            getChildren().add(index, hbox);
        }
        return hbox;
    }

    private Node createAmountContent() {
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().addAll(amountInput, amountUnitLabel);
        return hbox;
    }

    private Node createUsageContent() {
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_LEFT);
        this.exampleContextMenu = new ContextMenu();
        for (String text : exampleTexts) {
            MenuItem item = new MenuItem(text);
            item.setOnAction(ev -> usageInput.setText(item.getText()));
            exampleContextMenu.getItems().add(item);
        }
        exampleLink.getStyleClass().add("example-link");
        exampleLink.setOnMousePressed(event -> doExample(event, exampleLink));
        hbox.getChildren().addAll(usageInput, exampleLink);
        return hbox;
    }

    private Node createDaysContent() {
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().addAll(daysInput, daysUnit);
        return hbox;
    }

    private Node createCategoryContent() {
        HBox hbox = new HBox(4);
        RadioButtonGroup<DrugCategory> categoryButtons = new RadioButtonGroup<>();
        categoryButtons.createRadioButton("内服", DrugCategory.Naifuku);
        categoryButtons.createRadioButton("屯服", DrugCategory.Tonpuku);
        categoryButtons.createRadioButton("外用", DrugCategory.Gaiyou);
        hbox.getChildren().addAll(categoryButtons.getButtons());
        categoryButtons.setValue(DrugCategory.Naifuku);
        category = categoryButtons.valueProperty();
        return hbox;
    }

    private void setNodeVisible(Node row, boolean visible) {
        row.setVisible(visible);
        row.setManaged(visible);
    }

    private void setDaysVisible(boolean visible) {
        setNodeVisible(daysRow, visible);
    }

    private void doExample(MouseEvent event, Node anchor) {
        exampleContextMenu.show(anchor, event.getScreenX(), event.getScreenY());
    }

    private void addLabelContextMenu() {
        drugNameLabel.setOnContextMenuRequested(event -> {
            ContextMenu menu = createDrugNameContextMenu();
            menu.show(drugNameLabel, event.getScreenX(), event.getScreenY());
        });
    }

    private ContextMenu createDrugNameContextMenu() {
        ContextMenu menu = new ContextMenu();
        {
            MenuItem item = new MenuItem("コピー");
            item.setOnAction(evt -> {
                String text = drugNameLabel.getText();
                GuiUtil.copyToClipboard(text);
            });
            menu.getItems().add(item);
        }
        return menu;
    }

    public String getAmountLabel() {
        return amountLabel.getText();
    }

    public void setAmountLabel(String amountLabel) {
        this.amountLabel.setText(amountLabel);
    }

    String getAmount() {
        return amountInput.getText();
    }

    void setAmount(double value) {
        amountInput.setText(amountFormatter.format(value));
    }

    void clearAmount() {
        amountInput.setText("");
    }

    String getUsage() {
        return usageInput.getText();
    }

    void setUsage(String usage) {
        usageInput.setText(usage);
    }

    void clearUsage() {
        usageInput.setText("");
    }

    DrugCategory getCategory() {
        return category.getValue();
    }

    void setCategory(DrugCategory category) {
        this.category.setValue(category);
    }

    void setDaysLabel(String label) {
        this.daysLabel.setText(label);
    }

    String getDays() {
        return daysInput.getText();
    }

    void setDays(int days) {
        daysInput.setText("" + days);
    }

    boolean isDaysEmpty() {
        return daysInput.getText().isEmpty();
    }

    void setDaysUnit(String unit) {
        this.daysUnit.setText(unit);
    }

    void addToDaysRow(Node node) {
        daysRow.getChildren().add(node);
    }

    void clearDays() {
        daysInput.setText("");
    }

    private void onCategoryChange(DrugCategory category) {
        if (category != null) {
            switch (category) {
                case Naifuku: {
                    setAmountLabel("用量：");
                    setDaysLabel("日数：");
                    setDaysUnit("日分");
                    setDaysVisible(true);
                    break;
                }
                case Tonpuku: {
                    setAmountLabel("一回：");
                    setDaysLabel("回数：");
                    setDaysUnit("回分");
                    setDaysVisible(true);
                    break;
                }
                case Gaiyou: {
                    setAmountLabel("用量：");
                    setDaysVisible(false);
                    break;
                }
            }
        }
    }

    private boolean isNotEmptyString(String s) {
        return s != null && !s.isEmpty();
    }

    private void adaptComment() {
        boolean visible = isNotEmptyString(commentLabel.getText());
        commentRow.setManaged(visible);
        commentRow.setVisible(visible);
    }

    private void adaptTekiyou() {
        boolean visible = isNotEmptyString(tekiyouLabel.getText());
        tekiyouRow.setManaged(visible);
        tekiyouRow.setVisible(visible);
    }

    private void adaptToDaysFixedChange(boolean selected){

    }
}
