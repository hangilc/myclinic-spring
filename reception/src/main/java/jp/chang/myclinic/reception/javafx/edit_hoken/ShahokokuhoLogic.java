package jp.chang.myclinic.reception.javafx.edit_hoken;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.dto.ShahokokuhoDTO;
import jp.chang.myclinic.util.logic.ErrorMessages;
import jp.chang.myclinic.util.logic.Logic;
import jp.chang.myclinic.util.logic.date.ValidFromLogic;
import jp.chang.myclinic.util.logic.date.ValidUptoLogic;

class ShahokokuhoLogic implements Logic<ShahokokuhoDTO> {

    //private static Logger logger = LoggerFactory.getLogger(ShahokokuhoLogic.class);
    private ShahokokuhoHokenshaBangouLogic hokenshaBangou = new ShahokokuhoHokenshaBangouLogic();
    private StringProperty hihokenshaKigou = new SimpleStringProperty();
    private StringProperty hihokenshaBangou = new SimpleStringProperty();
    private IntegerProperty honninKazoku = new SimpleIntegerProperty();
    private ValidFromLogic validFrom = new ValidFromLogic();
    private ValidUptoLogic validUpto = new ValidUptoLogic();
    private IntegerProperty kourei = new SimpleIntegerProperty();

    ShahokokuhoLogic() {
        honninKazoku.setValue(0);
        validFrom.setGengou(Gengou.Current);
        validUpto.setGengou(Gengou.Current);
        kourei.setValue(0);
    }

    public StringProperty hokenshaBangouProperty(){
        return hokenshaBangou.getInput();
    }

    public StringProperty hihokenshaKigouProperty(){
        return hihokenshaKigou;
    }

    public StringProperty hihokenshaBangouProperty(){
        return hihokenshaBangou;
    }

    public IntegerProperty honninKazokuProperty(){
        return honninKazoku;
    }

    public ValidFromLogic validFromLogic(){
        return validFrom;
    }

    public ValidUptoLogic validUptoLogic(){
        return validUpto;
    }

    public IntegerProperty koureiProperty(){
        return kourei;
    }

    @Override
    public ShahokokuhoDTO getValue(ErrorMessages em) {
        return null;
    }

    @Override
    public void setValue(ShahokokuhoDTO value, ErrorMessages em) {

    }

}
