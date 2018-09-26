package jp.chang.myclinic.practice.javafx.drug;

import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.PrescExampleDTO;
import jp.chang.myclinic.dto.PrescExampleFullDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DrugInput extends InputBase {

    private static Logger logger = LoggerFactory.getLogger(DrugInput.class);
    private Label commentLabel = new Label();
    private HBox commentRow;
    private Label tekiyouLabel = new Label();
    private HBox tekiyouRow;

    DrugInput() {
        this.commentRow = addRowBeforeCategory(new Label("注釈："), commentLabel);
        adaptComment();
        commentLabel.textProperty().addListener((obs, oldValue, newValue) -> adaptComment());
        this.tekiyouRow = addRowBeforeCategory(new Label("摘要："), tekiyouLabel);
        adaptTekiyou();
        tekiyouLabel.textProperty().addListener((obs, oldValue, newValue) -> adaptTekiyou());
    }

    void setDrug(DrugFullDTO drugFull){
        setDrug(drugFull, false);
    }

    void setDrug(DrugFullDTO drugFull, boolean fixedDays){
        setMaster(drugFull.master);
        DrugDTO drug = drugFull.drug;
        setAmount(drug.amount);
        setUsage(drug.usage);
        setCategory(DrugCategory.fromCode(drug.category));
        if( getCategory() != DrugCategory.Gaiyou){
            if( isDaysEmpty() || !fixedDays ) {
                setDays(drug.days);
            }
        }
    }

    void setExample(PrescExampleFullDTO exampleFull){
        setExample(exampleFull, false);
    }

    void setExample(PrescExampleFullDTO exampleFull, boolean fixedDays){
        setMaster(exampleFull.master);
        PrescExampleDTO example = exampleFull.prescExample;
        try {
            double amount = Double.parseDouble(example.amount);
            setAmount(amount);
        } catch(NumberFormatException ex){
            logger.error("Invalid amount: " + example.amount);
            clearAmount();
        }
        setUsage(example.usage);
        setCategory(DrugCategory.fromCode(example.category));
        if( getCategory() != DrugCategory.Gaiyou){
            if( isDaysEmpty() || !fixedDays ) {
                setDays(example.days);
            }
        }
    }

    void setComment(String comment){
        commentLabel.setText(comment);
    }

    void clearComment(){
        commentLabel.setText("");
    }

    void setTekiyou(String tekiyou){
        tekiyouLabel.setText(tekiyou);
    }

    void clearTekiyou(){
        tekiyouLabel.setText("");
    }

    StringProperty tekiyouProperty(){
        return tekiyouLabel.textProperty();
    }

    private boolean isNotEmptyString(String s){
        return s != null && !s.isEmpty();
    }

    private void adaptComment(){
        boolean visible = isNotEmptyString(commentLabel.getText());
        commentRow.setManaged(visible);
        commentRow.setVisible(visible);
    }

    private void adaptTekiyou(){
        boolean visible = isNotEmptyString(tekiyouLabel.getText());
        tekiyouRow.setManaged(visible);
        tekiyouRow.setVisible(visible);
    }

}
