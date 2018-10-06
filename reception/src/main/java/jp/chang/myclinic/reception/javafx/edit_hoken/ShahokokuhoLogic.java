package jp.chang.myclinic.reception.javafx.edit_hoken;

import javafx.beans.property.*;
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
    private ObjectProperty<Integer> honninKazoku = new SimpleObjectProperty<Integer>();
    private ValidFromLogic validFrom = new ValidFromLogic();
    private ValidUptoLogic validUpto = new ValidUptoLogic();
    private ObjectProperty<Integer> kourei = new SimpleObjectProperty<Integer>();

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

    public ObjectProperty<Integer> honninKazokuProperty(){
        return honninKazoku;
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
        return null;
    }

    @Override
    public void setValue(ShahokokuhoDTO value, ErrorMessages em) {

    }

}
