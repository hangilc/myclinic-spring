package jp.chang.myclinic.practice.javafx.drug;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.DrugAttrDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.util.DrugUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DrugDisp extends TextFlow {

    private final DrugFullDTO drug;
    private Runnable onClickHandler = () -> {};

    public DrugDisp(int index, DrugFullDTO drug, DrugAttrDTO attr){
        this.drug = drug;
        String text = String.format("%d)%s", index, DrugUtil.drugRep(drug));
        if( attr != null && attr.tekiyou != null && !attr.tekiyou.isEmpty() ){
            text += " [摘要：" + attr.tekiyou + "]";
        }
        getChildren().add(new Text(text));
        setOnMouseClicked(evt -> onClickHandler.run());
    }

    public DrugFullDTO getDrug(){
        return drug;
    }

    public void setOnClickHandler(Runnable handler){
        this.onClickHandler = handler;
    }

}
