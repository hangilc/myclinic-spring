package jp.chang.myclinic.util.dto_logic;

import jp.chang.myclinic.util.StringUtil;

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

}
