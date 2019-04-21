package jp.chang.myclinic.practice.javafx.drug.lib;

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
import jp.chang.myclinic.consts.Zaikei;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.util.validator.Validated;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.RadioButtonGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;

import static jp.chang.myclinic.consts.DrugCategory.Naifuku;

class DrugInputBase extends VBox {

    private int iyakuhincode;
    private Text drugName = new Text("");
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

    public DrugInputBase() {
        super(4);
        getStyleClass().add("drug-input");
        amountInput.getStyleClass().add("amount-input");
        daysInput.getStyleClass().add("days-input");
        addRow(new Label("名称："), new TextFlow(drugName));
        addRow(amountLabel, createAmountContent());
        addRow(new Label("用法："), createUsageContent());
        daysRow = addRow(daysLabel, createDaysContent());
        this.categoryRow = addRow(createCategoryContent());
        category.addListener((obs, oldValue, newValue) -> onCategoryChange(oldValue, newValue));
        category.setValue(null);
        category.setValue(Naifuku);
        addLabelContextMenu();
        this.commentRow = addRowBeforeCategory(new Label("注釈："), commentLabel);
        setCommentVisible(false);
    }

    public void clear(){
        iyakuhincode = 0;
        drugName.setText("");
        amountInput.setText("");
        amountUnitLabel.setText("");
        usageInput.setText("");
        clearDays();
        commentLabel.setText("");
        setCommentVisible(false);
    }

    private HBox addRow(Label label, Node content) {
        return addRowBefore(label, content, null);
    }

    HBox addRowBeforeCategory(Label label, Node content) {
        return addRowBefore(label, content, categoryRow);
    }

    Node addRow(Node content) {
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
        categoryButtons.createRadioButton("内服", Naifuku);
        categoryButtons.createRadioButton("屯服", DrugCategory.Tonpuku);
        categoryButtons.createRadioButton("外用", DrugCategory.Gaiyou);
        hbox.getChildren().addAll(categoryButtons.getButtons());
        categoryButtons.setValue(Naifuku);
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
        drugName.setOnContextMenuRequested(event -> {
            ContextMenu menu = createDrugNameContextMenu();
            menu.show(drugName, event.getScreenX(), event.getScreenY());
        });
    }

    private ContextMenu createDrugNameContextMenu() {
        ContextMenu menu = new ContextMenu();
        {
            MenuItem item = new MenuItem("コピー");
            item.setOnAction(evt -> {
                String text = drugName.getText();
                GuiUtil.copyToClipboard(text);
            });
            menu.getItems().add(item);
        }
        return menu;
    }

    public int getIyakuhincode() {
        return iyakuhincode;
    }

    private void setIyakuhincode(int iyakuhincode) {
        this.iyakuhincode = iyakuhincode;
    }

    private String getDrugName() {
        return drugName.getText();
    }

    private void setDrugName(String drugName) {
        this.drugName.setText(drugName);
    }

    public String getAmountLabel() {
        return amountLabel.getText();
    }

    private void setAmountLabel(String amountLabel) {
        this.amountLabel.setText(amountLabel);
    }

    String getAmount() {
        return amountInput.getText();
    }

    void setAmount(double value) {
        setAmount(amountFormatter.format(value));
    }

    void setAmount(String input) {
        amountInput.setText(input);
    }

    void clearAmount() {
        amountInput.setText("");
    }

    private void setAmountUnit(String unit) {
        this.amountUnitLabel.setText(unit);
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

    public DrugCategory getCategory() {
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
        setDays("" + days);
    }

    void setDays(String input) {
        daysInput.setText(input);
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

    void setComment(String comment){
        this.commentLabel.setText(comment);
    }

    void setCommentVisible(boolean visible){
        setNodeVisible(this.commentRow, visible);
    }

    void categoryChangeHook(DrugCategory prevCategory, DrugCategory newCategory){

    }

    private void onCategoryChange(DrugCategory prevCategory, DrugCategory newCategory) {
        if (newCategory != null) {
            switch (newCategory) {
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
        categoryChangeHook(prevCategory, newCategory);
    }

    void clearMaster(){
        this.iyakuhincode = 0;
        setDrugName("");
        setAmountUnit("");
    }

    private boolean isNotEmptyString(String s) {
        return s != null && !s.isEmpty();
    }

    void setDrugData(String amount, String usage, String days){
        setAmount(amount);
        setUsage(usage);
        setDays(days);
    }

    private void setData(IyakuhinMasterDTO master,
                         DrugCategory category,
                         String amount,
                         String usage,
                         String days,
                         String comment) {
        setIyakuhincode(master.iyakuhincode);
        setDrugName(master.name);
        setAmountUnit(master.unit);
        setCategory(category);
        setDrugData(amount, usage, days);
        setComment(comment);
        setCommentVisible(isNotEmptyString(comment));
    }

    public void setMaster(IyakuhinMasterDTO master) {
        DrugCategory category = DrugCategory.Naifuku;
        if (Zaikei.fromCode(master.zaikei) == Zaikei.Gaiyou) {
            category = DrugCategory.Gaiyou;
        }
        setData(master, category, "", "", "", "");
    }

    public void updateMaster(IyakuhinMasterDTO master){
        setIyakuhincode(master.iyakuhincode);
        setDrugName(master.name);
        setAmountUnit(master.unit);
    }

    public void setPrescExample(PrescExampleFullDTO exampleFull) {
        PrescExampleDTO example = exampleFull.prescExample;
        DrugCategory exampleCategory = DrugCategory.fromCode(example.category);
        setData(exampleFull.master, exampleCategory, example.amount, example.usage, example.days + "",
                example.comment);
    }

    public void setDrug(DrugFullDTO drugFull){
        DrugDTO drug = drugFull.drug;
        DrugCategory drugCategory = DrugCategory.fromCode(drug.category);
        setData(drugFull.master, drugCategory, formatAmount(drug.amount), drug.usage, drug.days + "", "");
    }

    private String formatAmount(double amountValue){
        DecimalFormat amountFormatter = new DecimalFormat("###.##");
        return amountFormatter.format(amountValue);
    }

}
