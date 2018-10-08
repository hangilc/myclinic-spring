package jp.chang.myclinic.reception.javafx.edit_hoken;

import jp.chang.myclinic.util.logic.ErrorMessages;
import jp.chang.myclinic.util.logic.PositiveIntegerLogic;
import jp.chang.myclinic.util.verify.HokenVerifierLib;

class HokenshaBangouLogic extends PositiveIntegerLogic {

    //private static Logger logger = LoggerFactory.getLogger(HokenshaBangouLogic.class);

    HokenshaBangouLogic(String name){
        super(name);
    }

    @Override
    public Integer getValue(ErrorMessages em) {
        int ne = em.getNumberOfErrors();
        Integer value = super.getValue(em);
        if( em.hasErrorSince(ne) ){
            return 0;
        }
        validateNumberOfDigits(value, em);
        if( em.hasErrorSince(ne) ){
            return 0;
        }
        if( !HokenVerifierLib.verifyHokenshaBangou(value) ){
            em.add(String.format("%sの検証番号が正しくありません。", getName()));
            return 0;
        }
        return value;
    }

    void validateNumberOfDigits(int value, ErrorMessages em){

    }
}
