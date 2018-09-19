package jp.chang.myclinic.practice.javafx.drug2;

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
import jp.chang.myclinic.dto.PrescExampleDTO;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.RadioButtonGroup;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class Input extends VBox {

    //private static Logger logger = LoggerFactory.getLogger(Input.class);

    public enum SetOption {
        None, IgnoreNull
    }

    private int iyakuhincode = 0;
    private int prescExampleId = 0;
    private int drugId = 0;
    private Text drugNameLabel = new Text("");
    private Label amountLabel = new Label("");
    private TextField amountInput = new TextField();
    private Label amountUnitLabel = new Label("");
    private TextField usageInput = new TextField();
    private Node daysRow;
    private Label daysLabel = new Label("");
    private TextField daysInput = new TextField();
    private Label daysUnit = new Label("");
    private ObjectProperty<DrugCategory> category;
    private DecimalFormat amountFormatter = new DecimalFormat("###.##");

    public Input() {
        super(4);
        getStyleClass().add("drug-input");
        amountInput.getStyleClass().add("amount-input");
        daysInput.getStyleClass().add("days-input");
        addRow(new Label("名称："), new TextFlow(drugNameLabel));
        addRow(amountLabel, createAmountContent());
        addRow(new Label("用法："), createUsageContent());
        daysRow = addRow(daysLabel, createDaysContent());
        addRow(createCategoryContent());
        category.setValue(null);
        category.setValue(DrugCategory.Naifuku);
    }

    public void setData(DrugData data){
        setData(data, SetOption.None);
    }

    public void setData(DrugData data, SetOption option){
        this.iyakuhincode = data.getIyakuhincode();
        this.prescExampleId = data.getPrescExampleId();
        this.drugId = data.getDrugId();
        drugNameLabel.setText(data.getName());
        setUnit(data.getUnit());
        {
            Double amount = data.getAmount();
            if( option == SetOption.IgnoreNull ){
                if( amount != null ){
                    setAmount(amount);
                }
            } else {
                setAmount(amount);
            }
        }
        {
            DrugCategory cat = data.getCategory();
            if( option == SetOption.IgnoreNull ){
                if( cat != null ){
                    category.setValue(cat);
                } else {
                    if (data.getZaikei() == Zaikei.Gaiyou) {
                        category.setValue(DrugCategory.Gaiyou);
                    } else {
                        if( category.getValue() == DrugCategory.Gaiyou ){
                            category.setValue(DrugCategory.Naifuku);
                        }
                    }
                }
            } else {
                if (data.getCategory() == null) {
                    if (data.getZaikei() == Zaikei.Gaiyou) {
                        category.setValue(DrugCategory.Gaiyou);
                    } else {
                        category.setValue(DrugCategory.Naifuku);
                    }
                } else {
                    category.setValue(data.getCategory());
                }
            }
        }
        {
            String usage = data.getUsage();
            if( option == SetOption.IgnoreNull ) {
                if( usage != null ){
                    setUsage(usage);
                }
            } else {
                setUsage(data.getUsage());
            }
        }
        {
            Integer days = data.getDays();
            if( option == SetOption.IgnoreNull ) {
                if (days != null) {
                    setDays(days);
                }
            } else {
                setDays(data.getDays());
            }
        }
    }

    public void clear(){
        this.iyakuhincode = 0;
        drugNameLabel.setText("");
        amountInput.setText("");
        amountUnitLabel.setText("");
        usageInput.setText("");
        daysInput.setText("");
        daysUnit.setText("");
        category.setValue(DrugCategory.Naifuku);
    }

    public int getPrescExampleId(){
        return prescExampleId;
    }

    public int getIyakuhincode() {
        return iyakuhincode;
    }

    public String getAmount(){
        return amountInput.getText();
    }

    public String getUsage(){
        return usageInput.getText();
    }

    public DrugCategory getCategory(){
        return category.getValue();
    }

    public String getDays(){
        return daysInput.getText();
    }

    private void setAmount(Double value) {
        if( value == null ){
            clearAmount();
        } else {
            amountInput.setText(amountFormatter.format(value));
        }
    }

    private void clearAmount() {
        amountInput.setText("");
    }

    private void setUnit(String unit){
        amountUnitLabel.setText(unit);
    }

    private void setUsage(String usage){
        usageInput.setText(usage == null ? "" : usage);
    }

    private void setDays(Integer days){
        daysInput.setText(days == null ? "" : days + "");
    }

    public Node addRow(Label label, Node content) {
        HBox hbox = new HBox(4);
        label.setMinWidth(Control.USE_PREF_SIZE);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().addAll(label, content);
        getChildren().add(hbox);
        return hbox;
    }

    private void addRow(Node content) {
        getChildren().add(content);
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

    public DrugDTO createDrug(){
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
        return dto;
    }

    public PrescExampleDTO createPrescExample(String comment) {
        PrescExampleDTO ex = new PrescExampleDTO();
        ex.prescExampleId = getPrescExampleId();
        ex.iyakuhincode = getIyakuhincode();
        if (ex.iyakuhincode == 0) {
            GuiUtil.alertError("医薬品が設定されていません。");
            return null;
        }
        ex.amount = getAmount();
        try {
            double value = Double.parseDouble(ex.amount);
            if (!(value > 0)) {
                GuiUtil.alertError("用量の値が正でありません。");
                return null;
            }
        } catch (NumberFormatException e) {
            GuiUtil.alertError("用量の入力が不適切です。");
            return null;
        }
        ex.usage = getUsage();
        DrugCategory category = getCategory();
        ex.category = category.getCode();
        if (category == DrugCategory.Gaiyou) {
            ex.days = 1;
        } else {
            try {
                ex.days = Integer.parseInt(getDays());
                if (!(ex.days > 0)) {
                    GuiUtil.alertError("日数の値が正の整数でありません。");
                    return null;
                }
            } catch (NumberFormatException e) {
                GuiUtil.alertError("日数の入力が不敵津です。");
                return null;
            }
        }
        ex.comment = comment;
        return ex;
    }

}
