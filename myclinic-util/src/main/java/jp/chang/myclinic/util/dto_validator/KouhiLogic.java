package jp.chang.myclinic.util.dto_validator;

import jp.chang.myclinic.util.logic.ErrorMessages;
import jp.chang.myclinic.util.logic.LogicUtil;

public class KouhiLogic extends LogicUtil {

    //private static Logger logger = LoggerFactory.getLogger(KouhiLogic.class);

    private KouhiLogic() {

    }

    public static void todoufukenIsValidInKouhiFutansha(Integer futanshaBangou, String name,
                                                        ErrorMessages em){
        int rem = futanshaBangou % 1000000;
        int todoufuken = rem / 10000;
        if( !HokenLib.isValidTodoufukenBangou(todoufuken) ){
            em.add(nameWith(name, "の") + "負担者番号の都道府県部分が不適切です。");
        }
    }

}
