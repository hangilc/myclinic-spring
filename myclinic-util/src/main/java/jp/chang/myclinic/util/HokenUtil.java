package jp.chang.myclinic.util;

import jp.chang.myclinic.dto.HokenDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hangil on 2017/05/21.
 */
public class HokenUtil {

    public static String hokenRep(HokenDTO hoken){
        List<String> terms = new ArrayList<>();
        if( hoken.shahokokuho != null ){
            terms.add(ShahokokuhoUtil.name(hoken.shahokokuho.hokenshaBangou));
            if( hoken.shahokokuho.kourei > 0 ){
                terms.add("高齢" + hoken.shahokokuho.kourei + "割");
            }
        }
        if( hoken.koukikourei != null ){
            terms.add(KoukikoureiUtil.rep(hoken.koukikourei));
        }
        if( hoken.roujin != null ){
            terms.add(RoujinUtil.rep(hoken.roujin));
        }
        if( hoken.kouhi1 != null ){
            terms.add(KouhiUtil.rep(hoken.kouhi1));
        }
        if( hoken.kouhi2 != null ){
            terms.add(KouhiUtil.rep(hoken.kouhi2));
        }
        if( hoken.kouhi3 != null ){
            terms.add(KouhiUtil.rep(hoken.kouhi3));
        }
        return String.join("・", terms);
    }

}
