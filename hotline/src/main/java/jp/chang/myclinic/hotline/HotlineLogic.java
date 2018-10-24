package jp.chang.myclinic.hotline;

import jp.chang.myclinic.util.logic.ErrorMessages;
import jp.chang.myclinic.util.logic.LogicUtil;

public class HotlineLogic extends LogicUtil {

    //private static Logger logger = LoggerFactory.getLogger(HotlineLogic.class);

    private HotlineLogic() {

    }

    public static void isNotTooLongToEnter(String message, String name, ErrorMessages em){
        if( message.length() > 140 ){
            em.add(nameWith(name, "の") + "文字数が多すぎます。");
        }
    }

    public static void hasNotTooManyLinesToEnter(String message, String name, ErrorMessages em){
        int count = 0;
        for(int i=0;i<message.length();i++){
            char ch = message.charAt(i);
            if( ch == '\n' ){
                count += 1;
            }
        }
        if( count > 10 ){
            em.add(nameWith(name, "の") + "行数が多すぎます。");
        }
    }

}
