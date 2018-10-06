package jp.chang.myclinic.reception.javafx.edit_hoken;

import jp.chang.myclinic.util.logic.ErrorMessages;

class ShahokokuhoHokenshaBangouLogic extends HokenshaBangouLogic {

    //private static Logger logger = LoggerFactory.getLogger(ShahokokuhoHokenshaBangouLogic.class);

    ShahokokuhoHokenshaBangouLogic() {
        super("保険者番号");
    }

    @Override
    void validateNumberOfDigits(int value, ErrorMessages em) {
        String s = String.format("%d", value);
        int n = s.length();
        if( n < 5 ){
            em.add(String.format("%sの桁数が少なすぎます。", getName()));
        } else if( n > 8 ){
            em.add(String.format("%sの桁数が多すぎます。", getName()));
        }
    }
}
