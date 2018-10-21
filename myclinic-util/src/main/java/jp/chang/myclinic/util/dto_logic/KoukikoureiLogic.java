package jp.chang.myclinic.util.dto_logic;

import jp.chang.myclinic.util.logic.ErrorMessages;
import jp.chang.myclinic.util.logic.Logic;
import jp.chang.myclinic.util.logic.LogicUtil;
import jp.chang.myclinic.util.logic.Validators;

public class KoukikoureiLogic extends LogicUtil {

    //private static Logger logger = LoggerFactory.getLogger(KoukikoureiLogic.class);

    private KoukikoureiLogic() {

    }

    public static Logic<Integer> isValidKoukikoureiHokenshaBangou(Logic<Integer> bangou) {
        return bangou
                .validate(Validators.hasDigitsInRange(8, 8))
                .validate(KoukikoureiLogic::verifyHouseiBangou)
                .validate(Validators::hasValidCheckingDigit);
    }

    private static void verifyHouseiBangou(Integer value, String name, ErrorMessages em) {
        int housei = value / 1000000;
        if (housei != 39) {
            em.add(nameWith(name, "の") + "法制番号が３９でありません。");
        }
    }


}
