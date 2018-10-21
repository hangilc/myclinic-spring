package jp.chang.myclinic.util.dto_logic;

import jp.chang.myclinic.util.StringUtil;
import jp.chang.myclinic.util.logic.Logic;
import jp.chang.myclinic.util.logic.Validators;

import static jp.chang.myclinic.util.logic.Validators.hasDigitsInRange;

public class ShahokokuhoLogic {

    //private static Logger logger = LoggerFactory.getLogger(ShahokokuhoLogic.class);

    private ShahokokuhoLogic() {

    }

    public static String formatHokenshaBangou(int bangou){
        String s = String.format("%d", bangou);
        if( s.length() < 6 ){
            return StringUtil.padLeft('0', s, 6);
        } else if( s.length() < 8 ){
            return StringUtil.padLeft('0', s, 8);
        } else {
            return s;
        }
    }

    public static Logic<Integer> isValidShahokokuhoHokenshaBangou(Logic<Integer> bangou){
        return bangou
                .validate(Validators::isPositive)
                .validate(hasDigitsInRange(5, 8))
                .validate(Validators::hasValidCheckingDigit);
    }

}
