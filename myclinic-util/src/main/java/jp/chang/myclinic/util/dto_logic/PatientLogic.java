package jp.chang.myclinic.util.dto_logic;

import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.util.kanjidate.KanjiDateRepBuilder;
import jp.chang.myclinic.util.logic.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PatientLogic extends LogicUtil {

    private static Logger logger = LoggerFactory.getLogger(PatientLogic.class);

    private PatientLogic() {

    }

    public static String birthdaySqldateToRep(String sqldate){
        ErrorMessages em = new ErrorMessages();
        String rep = new LogicValue<>(sqldate)
                .validate(Validators::isNotNull)
                .validate(Validators::isNotEmpty)
                .convert(Converters::sqldateToLocalDate)
                .map(date -> new KanjiDateRepBuilder(date).format1().build())
                .getValue(null, em);
        if( em.hasError() ){
            logger.error(em.getMessage(""));
            return "";
        } else {
            return rep;
        }
    }

}
