package jp.chang.myclinic.utilfx.dateinput;

import jp.chang.myclinic.util.logic.ErrorMessages;

import java.time.LocalDate;

public class ValidFromLogic extends DateLogic {

    //private static Logger logger = LoggerFactory.getLogger(ValidFromLogic.class);

    public static boolean validateRange(LocalDate validFrom, LocalDate validUpto, ErrorMessages em){
        if( validFrom.isEqual(validUpto) || validFrom.isBefore(validUpto) ){
            return true;
        } else {
            em.add("有効期限が資格取得日より前の日付になっています。");
            return false;
        }
    }
}
