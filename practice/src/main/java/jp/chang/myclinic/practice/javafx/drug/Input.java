package jp.chang.myclinic.practice.javafx.drug;

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
import jp.chang.myclinic.dto.DrugAttrDTO;
import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import jp.chang.myclinic.utilfx.RadioButtonGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

class Input extends VBox {

    private static Logger logger = LoggerFactory.getLogger(Input.class);

    private int iyakuhincode = 0;
    private Text drugNameLabel = new Text("");
    private Label amountLabel = new Label("");
    private TextField amountInput = new TextField();
    private Label amountUnitLabel = new Label("");
    private TextField usageInput = new TextField();
    private Node daysRow;
    private Label daysLabel = new Label("");
    private TextField daysInput = new TextField();
    private Label daysUnit = new Label("");
    private Text commentText = new Text(null);
    private Text tekiyouText = new Text(null);
    private ObjectProperty<DrugCategory> category;
    private DecimalFormat amountFormatter = new DecimalFormat("###.##");

    Input() {
        super(4);
        getStyleClass().add("drug-input");
        amountInput.getStyleClass().add("amount-input");
        daysInput.getStyleClass().add("days-input");
        addRow(new Label("名称："), new TextFlow(drugNameLabel));
        addRow(amountLabel, createAmountContent());
        addRow(new Label("用法："), createUsageContent());
        daysRow = addRow(daysLabel, createDaysContent());
        Node commentRow = addRow(new Label("注釈："), new TextFlow(commentText));
        Node tekiyouRow = addRow(new Label("摘要："), new TextFlow(tekiyouText));
        addRow(createCategoryContent());
        category.setValue(null);
        category.setValue(DrugCategory.Naifuku);
        setNodeVisible(commentRow, false);
        commentText.textProperty().addListener((obs, oldValue, newValue) -> {
            setNodeVisible(commentRow, newValue != null);
        });
        setNodeVisible(tekiyouRow, false);
        tekiyouText.textProperty().addListener((obs, oldValue, newValue) -> {
            setNodeVisible(tekiyouRow, newValue != null);
        });
    }

    void setMaster(IyakuhinMasterDTO master){
        this.iyakuhincode = master.iyakuhincode;
        drugNameLabel.setText(master.name);
        amountUnitLabel.setText(master.unit);
        if( Zaikei.fromCode(master.zaikei) == Zaikei.Gaiyou ){
            category.setValue(DrugCategory.Gaiyou);
        } else {
            if( category.getValue() == DrugCategory.Gaiyou ){
                category.setValue(DrugCategory.Naifuku);
            }
        }
    }

    void clearMaster(){
        this.iyakuhincode = 0;
        drugNameLabel.setText("");
        amountUnitLabel.setText("");
    }

    void setDrug(DrugFullDTO drug, DrugAttrDTO attr){
        setMaster(drug.master);
        setAmount(drug.drug.amount);
        setUsage(drug.drug.usage);
        setDays(drug.drug.days);
        setCategory(drug.drug.category);
        commentText.setText(null);
        tekiyouText.setText(attr == null ? null : attr.tekiyou);
    }

    Node addRow(Label label, Node content) {
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().addAll(label, content);
        getChildren().add(hbox);
        return hbox;
    }

    void addRow(Node content) {
        getChildren().add(content);
    }

    void setUsage(String usage){
        usageInput.setText(usage);
    }

    void setDays(int days){
        daysInput.setText("" + days);
    }

    void clearDays(){
        daysInput.setText("");
    }

    void setCategory(int code){
        DrugCategory cat = DrugCategory.fromCode(code);
        if( cat == null ){
            logger.error("Invalid category: {}", code);
        } else {
            category.setValue(cat);
        }
    }

    void setAmount(double value){
        amountInput.setText(amountFormatter.format(value));
   }

   void clearAmount(){
        amountInput.setText("");
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

    private void setNodeVisible(Node row, boolean visible){
        row.setVisible(visible);
        row.setManaged(visible);
    }

    private void setDaysVisible(boolean visible){
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
            item.setOnAction(ev -> setUsage(item.getText()));
            contextMenu.getItems().add(item);
        });
        contextMenu.show(anchor, event.getScreenX(), event.getScreenY());
    }

    private DrugCategory getCategory(){
        return category.getValue();
    }

    public void convertToDrug(int drugId, int visitId, int prescribed,
                              BiConsumer<DrugDTO, List<String>> cb) {
        List<String> err = new ArrayList<>();
        DrugDTO drug = new DrugDTO();
        drug.drugId = drugId;
        drug.iyakuhincode = iyakuhincode;
        if( drug.iyakuhincode == 0 ){
            err.add("医薬品が指定されていません。");
        }
        try {
            drug.amount = Double.parseDouble(amountInput.getText());
        } catch (NumberFormatException ex) {
            err.add("用量の入力が適切でありません。");
        }
        drug.usage = usageInput.getText();
        if( drug.usage == null || drug.usage.isEmpty() ){
            err.add("用法が入力されていません。");
        }
        DrugCategory category = getCategory();
        if (category != null) {
            drug.category = category.getCode();
        } else {
            err.add("Drug category is not specified.");
        }
        try {
            if (category == DrugCategory.Gaiyou) {
                drug.days = 1;
            } else {
                drug.days = Integer.parseInt(daysInput.getText());
            }
        } catch (NumberFormatException ex) {
            err.add("日数・回数の入力が不適切です。");
        }
        drug.visitId = visitId;
        if( drug.visitId == 0 ) {
            err.add("Invalid visitId.");
        }
        drug.prescribed = prescribed;
        if( !(drug.prescribed == 0 || drug.prescribed == 1) ){
            err.add("Invalid prescribed value.");
        }
        cb.accept(drug, err);
    }

}
