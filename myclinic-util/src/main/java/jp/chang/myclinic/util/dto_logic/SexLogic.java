package jp.chang.myclinic.util.dto_logic;

import jp.chang.myclinic.consts.Sex;
import jp.chang.myclinic.util.logic.ErrorMessages;
import jp.chang.myclinic.util.logic.LogicUtil;

public class SexLogic extends LogicUtil {

    //private static Logger logger = LoggerFactory.getLogger(SexLogic.class);

    private SexLogic() {

    }

    public static Sex codeToSex(String code, String name, ErrorMessages em){
        Sex sex = Sex.fromCode(code);
        if( sex != null ){
            return sex;
        } else {
            em.add(nameWith(name, "の") + "性別のコードが不適切です。");
            return null;
        }
    }

}
