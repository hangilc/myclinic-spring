package jp.chang.myclinic.reception.javafx.edit_hoken;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.dto.ShahokokuhoDTO;
import jp.chang.myclinic.util.logic.ErrorMessages;
import jp.chang.myclinic.util.logic.IntegerChoiceLogic;
import jp.chang.myclinic.util.logic.Logic;
import jp.chang.myclinic.util.logic.StringLogic;
import jp.chang.myclinic.util.logic.date.ValidFromLogic;
import jp.chang.myclinic.util.logic.date.ValidUptoLogic;

class ShahokokuhoLogic implements Logic<ShahokokuhoDTO> {

    //private static Logger logger = LoggerFactory.getLogger(ShahokokuhoLogic.class);
    private ShahokokuhoHokenshaBangouLogic hokenshaBangou = new ShahokokuhoHokenshaBangouLogic();
    private StringLogic hihokenshaKigou = new StringLogic();
    private StringLogic hihokenshaBangou = new StringLogic();
    private IntegerChoiceLogic honninKazoku = new IntegerChoiceLogic("本人・家族", 0, 1);
    private ValidFromLogic validFrom = new ValidFromLogic();
    private ValidUptoLogic validUpto = new ValidUptoLogic();
    private ObjectProperty<Integer> kourei = new SimpleObjectProperty<Integer>();

    ShahokokuhoLogic() {
        honninKazoku.valueProperty().setValue(0);
        validFrom.setGengou(Gengou.Current);
        validUpto.setGengou(Gengou.Current);
        kourei.setValue(0);
    }

    public StringProperty hokenshaBangouProperty(){
        return hokenshaBangou.getInput();
    }

    public StringProperty hihokenshaKigouProperty(){
        return hihokenshaKigou.stringProperty();
    }

    public StringProperty hihokenshaBangouProperty(){
        return hihokenshaBangou.stringProperty();
    }

    public IntegerProperty honninKazokuProperty(){
        return honninKazoku.valueProperty();
    }

    public ValidFromLogic validFromLogic(){
        return validFrom;
    }

    public ValidUptoLogic validUptoLogic(){
        return validUpto;
    }

    public ObjectProperty<Integer> koureiProperty(){
        return kourei;
    }

    @Override
    public ShahokokuhoDTO getValue(ErrorMessages em) {
        ShahokokuhoDTO value = new ShahokokuhoDTO();
        value.hokenshaBangou = hokenshaBangou.getValue(em);
        int ne = em.getNumberOfErrors();
        value.hihokenshaKigou = hihokenshaKigou.getValue(em);
        value.hihokenshaBangou = hihokenshaBangou.getValue(em);
        if( em.hasNoErrorSince(ne) ) {
            if( value.hihokenshaKigou.isEmpty() && value.hihokenshaBangou.isEmpty() ){
                em.add("被保険者記号と被保険者番号が両方とも空白です。");
            }
        }
        value.honnin = honninKazoku.getValue(em);
        return value;
    }

    @Override
    public void setValue(ShahokokuhoDTO value, ErrorMessages em) {

    }

}
