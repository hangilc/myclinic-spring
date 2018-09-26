package jp.chang.myclinic.practice.javafx.drug;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
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
        addLabelContextMenu();
    }

    void setMaster(IyakuhinMasterDTO master){
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

    void clearMaster(){
        this.iyakuhincode = 0;
        drugNameLabel.setText("");
        amountUnitLabel.setText("");
    }

    int getIyakuhincode() {
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
        Hyperlink exampleLink = new Hyperlink("例");
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
        category.addListener((obs, oldValue, newValue) -> adaptToCategory());
        return hbox;
    }

    private void setNodeVisible(Node row, boolean visible) {
        row.setVisible(visible);
        row.setManaged(visible);
    }

    private void setDaysVisible(boolean visible) {
        setNodeVisible(daysRow, visible);
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

}
