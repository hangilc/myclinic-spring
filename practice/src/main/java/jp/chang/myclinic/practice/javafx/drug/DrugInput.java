package jp.chang.myclinic.practice.javafx.drug;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.dto.DrugFullDTO;

class DrugInput extends InputBase {

    //private static Logger logger = LoggerFactory.getLogger(DrugInput.class);
    private int drugId;
    private Label commentLabel = new Label();
    private HBox commentRow;
    private Label tekiyouLabel = new Label();
    private HBox tekiyouRow;

    DrugInput() {
        this.commentRow = addRow(new Label("注釈："), commentLabel);
        adaptComment();
        commentLabel.textProperty().addListener((obs, oldValue, newValue) -> adaptComment());
        this.tekiyouRow = addRow(new Label("摘要："), tekiyouLabel);
        adaptTekiyou();
        tekiyouLabel.textProperty().addListener((obs, oldValue, newValue) -> adaptTekiyou());
    }

    void setDrug(DrugFullDTO drugFull){
        setDrug(drugFull, false);
    }

    void setDrug(DrugFullDTO drugFull, boolean fixedDays){
        this.drugId = drugFull.drug.drugId;
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

    private void adaptComment(){
        boolean visible = !commentLabel.getText().isEmpty();
        commentRow.setManaged(visible);
        commentRow.setVisible(visible);
    }

    private void adaptTekiyou(){
        boolean visible = !tekiyouLabel.getText().isEmpty();
        tekiyouRow.setManaged(visible);
        tekiyouRow.setVisible(visible);
    }

}
