package jp.chang.myclinic.util.logic.date;

import jp.chang.myclinic.util.logic.ErrorMessages;

import java.time.LocalDate;

public class ValidFromLogic extends DateLogic {

    //private static Logger logger = LoggerFactory.getLogger(ValidFromLogic.class);

    @FunctionalInterface
    public interface ValidRangeConsumer {
        void accept(LocalDate validFrom, LocalDate validUpto);
    }

    @FunctionalInterface
    public interface ValidRangeStorageValuesConsumer {
        void accept(String validFrom, String validUpto);
    }

    public static void validateRange(ValidFromLogic validFromLogic, ValidUptoLogic validUptoLogic, ErrorMessages em,
                                     ValidRangeConsumer handler){
        int ne = em.getNumberOfErrors();
        LocalDate validFrom = validFromLogic.getValue(em);
        LocalDate validUpto = validUptoLogic.getValue(em);
        if( em.hasErrorSince(ne) ){
            return;
        }
        if( validFrom.isEqual(validUpto) || validFrom.isBefore(validUpto) ){
            handler.accept(validFrom, validUpto);
        } else {
            em.add("有効期限が資格取得日より前の日付になっています。");
        }
    }

    public static void validateRangeToStorageValues(ValidFromLogic validFromLogic, ValidUptoLogic validUptoLogic, ErrorMessages em,
                                                    ValidRangeStorageValuesConsumer handler){
        validateRange(validFromLogic, validUptoLogic, em, (validFrom, validUpto) -> {
            int ne = em.getNumberOfErrors();
            String validFromStorage = validFromLogic.toStorageValue(validFrom, em);
            String validUptoStorage = validUptoLogic.toStorageValue(validUpto, em);
            if( em.hasNoErrorSince(ne) ){
                handler.accept(validFromStorage, validUptoStorage);
            }
        });
    }
}
