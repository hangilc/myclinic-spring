package jp.chang.myclinic.util.dto_validator;

import jp.chang.myclinic.util.logic.BiLogicValue;
import jp.chang.myclinic.util.logic.ErrorMessages;

import static jp.chang.myclinic.util.logic.BiValidators.validInterval;
import static jp.chang.myclinic.util.logic.Converters.sqldateToLocalDate;
import static jp.chang.myclinic.util.logic.Validators.*;

public class ValidatorLib {

    //private static Logger logger = LoggerFactory.getLogger(ValidatorLib.class);

    private ValidatorLib() {

    }

    public static void checkValidInterval(String validFromSqldate, String validUptoSqldate,
                                          String validFromName, String validUptoName,
                                          ErrorMessages em){
        new BiLogicValue<>(validFromSqldate, validUptoSqldate)
                .validate(isNotNull(), valid())
                .validate(isNotEmpty(), valid())
                .convert(sqldateToLocalDate())
                .validate(validInterval())
                .verify(validFromName, validUptoName, em);
    }

}
