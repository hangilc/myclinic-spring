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
import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.RadioButtonGroup;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

class InputBase extends VBox {

    //private static Logger logger = LoggerFactory.getLogger(InputBase.class);
    private int iyakuhincode = 0;
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
    private DrugInputBaseStateController controller = new DrugInputBaseStateController();
    private Hyperlink exampleLink = new Hyperlink("例");

    InputBase() {
        super(4);
        getStyleClass().add("drug-input");
        amountInput.getStyleClass().add("amount-input");
        daysInput.getStyleClass().add("days-input");
        addRow(new Label("名称："), new TextFlow(drugNameLabel));
        addRow(amountLabel, createAmountContent());
        addRow(new Label("用法："), createUsageContent());
        daysRow = addRow(daysLabel, createDaysContent());
        this.categoryRow = addRow(createCategoryContent());
        category.setValue(null);
        category.setValue(DrugCategory.Naifuku);
        category.addListener((obs, oldValue, newValue) -> {
            DrugInputBaseState state = new DrugInputBaseState();
            getState(state);
            controller.adaptToCategory(state);
            setState(state);
        });
        addLabelContextMenu();
    }

    public void setState(DrugInputBaseState state){
        this.iyakuhincode = state.getIyakuhincode();
        drugNameLabel.setText(state.getDrugName());
        amountLabel.setText(state.getAmountLabel());
        amountInput.setText(state.getAmount());
        amountUnitLabel.setText(state.getAmountUnit());
        usageInput.setText(state.getUsage());
        daysLabel.setText(state.getDaysLabel());
        daysInput.setText(state.getDays());
        daysUnit.setText(state.getDaysUnit());
        category.setValue(state.getCategory());
        setDaysVisible(state.isDaysVisible());
    }

    public void getState(DrugInputBaseState state){
        state.setIyakuhincode(iyakuhincode);
        state.setDrugName(drugNameLabel.getText());
        state.setAmountLabel(amountLabel.getText());
        state.setAmount(amountInput.getText());
        state.setAmountUnit(amountUnitLabel.getText());
        state.setUsage(usageInput.getText());
        state.setDaysLabel(daysLabel.getText());
        state.setDays(daysInput.getText());
        state.setDaysUnit(daysUnit.getText());
        state.setCategory(category.getValue());
        state.setDaysVisible(isDaysVisible());
    }

    public void simulateSelectCategory(DrugCategory newCategory){
        category.setValue(newCategory);
    }

    public void simulateClickExample(){
        exampleLink.fire();
    }

    private boolean isDaysVisible(){
        return daysRow.isVisible();
    }

    public void setMaster(IyakuhinMasterDTO master){
        this.iyakuhincode = master.iyakuhincode;
        drugNameLabel.setText(master.name);
        amountUnitLabel.setText(master.unit);
        if (Zaikei.fromCode(master.zaikei) == Zaikei.Gaiyou) {
            category.setValue(DrugCategory.Gaiyou);
        } else {
            if (category.getValue() == DrugCategory.Gaiyou) {
                category.setValue(DrugCategory.Naifuku);
            }
        }
    }

    public void clearMaster(){
        this.iyakuhincode = 0;
        drugNameLabel.setText("");
        amountUnitLabel.setText("");
    }

    public int getIyakuhincode() {
        return iyakuhincode;
    }

    String getAmount(){
        return amountInput.getText();
    }

    void setAmount(double value) {
        amountInput.setText(amountFormatter.format(value));
    }

    void clearAmount(){
        amountInput.setText("");
    }

    String getUsage(){
        return usageInput.getText();
    }

    void setUsage(String usage){
        usageInput.setText(usage);
    }

    void clearUsage(){
        usageInput.setText("");
    }

    DrugCategory getCategory(){
        return category.getValue();
    }

    void setCategory(DrugCategory category){
        this.category.setValue(category);
    }

    String getDays(){
        return daysInput.getText();
    }

    void setDays(int days){
        daysInput.setText("" + days);
    }

    boolean isDaysEmpty(){
        return daysInput.getText().isEmpty();
    }

    void addToDaysRow(Node node){
        daysRow.getChildren().add(node);
    }

    void clearDays(){
        daysInput.setText("");
    }

    HBox addRow(Label label, Node content) {
        return addRowBefore(label, content, null);
    }

    HBox addRowBeforeCategory(Label label, Node content){
        return addRowBefore(label, content, categoryRow);
    }

    Node addRow(Node content) {
        getChildren().add(content);
        return content;
    }

    private HBox addRowBefore(Label label, Node content, Node beforeNode){
        HBox hbox = new HBox(4);
        label.setMinWidth(Control.USE_PREF_SIZE);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().addAll(label, content);
        if( beforeNode == null ){
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
        //category.addListener((obs, oldValue, newValue) -> adaptToCategory());
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
        ContextMenu contextMenu = new ContextMenu();
        List<String> examples = Arrays.asList(
                "分１　朝食後",
                "分２　朝夕食後",
                "分３　毎食後",
                "分１　寝る前"
        );
        examples.forEach(ex -> {
            MenuItem item = new MenuItem(ex);
            item.setOnAction(ev -> usageInput.setText(item.getText()));
            contextMenu.getItems().add(item);
        });
        contextMenu.show(anchor, event.getScreenX(), event.getScreenY());
    }

    private void addLabelContextMenu(){
        drugNameLabel.setOnContextMenuRequested(event -> {
            ContextMenu menu = createDrugNameContextMenu();
            menu.show(drugNameLabel, event.getScreenX(), event.getScreenY());
        });
    }

    private ContextMenu createDrugNameContextMenu(){
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

    public DrugDTO createDrug(int drugId, int visitId, int prescribed) {
        DrugDTO dto = new DrugDTO();
        dto.drugId = drugId;
        dto.visitId = visitId;
        dto.iyakuhincode = getIyakuhincode();
        if (dto.iyakuhincode == 0) {
            GuiUtil.alertError("医薬品が設定されていません。");
            return null;
        }
        try {
            dto.amount = Double.parseDouble(getAmount());
            if (!(dto.amount > 0)) {
                GuiUtil.alertError("用量の値が正でありません。");
                return null;
            }
        } catch (NumberFormatException e) {
            GuiUtil.alertError("用量の入力が不適切です。");
            return null;
        }
        dto.usage = getUsage();
        DrugCategory category = getCategory();
        dto.category = category.getCode();
        if (category == DrugCategory.Gaiyou) {
            dto.days = 1;
        } else {
            try {
                dto.days = Integer.parseInt(getDays());
                if (!(dto.days > 0)) {
                    GuiUtil.alertError("日数の値が正の整数でありません。");
                    return null;
                }
            } catch (NumberFormatException e) {
                GuiUtil.alertError("日数の入力が不敵津です。");
                return null;
            }
        }
        dto.prescribed = prescribed;
        return dto;
    }

}
