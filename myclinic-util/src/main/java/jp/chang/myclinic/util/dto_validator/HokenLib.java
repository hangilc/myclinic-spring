package jp.chang.myclinic.util.dto_validator;

public class HokenLib {

    //private static Logger logger = LoggerFactory.getLogger(HokenLib.class);

    private HokenLib() {

    }

    public static boolean isValidTodoufukenBangou(int bangou) {
        return bangou >= 1 && bangou <= 47;
    }

}
