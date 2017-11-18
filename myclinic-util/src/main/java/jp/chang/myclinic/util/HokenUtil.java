package jp.chang.myclinic.util;

import jp.chang.myclinic.dto.HokenDTO;
import jp.chang.myclinic.dto.KouhiDTO;

import java.util.ArrayList;
import java.util.List;

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

    public static int calcFutanWari(HokenDTO hoken, int rcptAge){
        int futanWari = 10;
        if( hoken.shahokokuho != null ){
            futanWari = calcShahokokuhoFutanWariByAge(rcptAge);
            if( hoken.shahokokuho.kourei > 0 ){
                futanWari = hoken.shahokokuho.kourei;
            }
        }
        if( hoken.koukikourei != null ){
            futanWari = hoken.koukikourei.futanWari;
        }
        if( hoken.roujin != null ){
            futanWari = hoken.roujin.futanWari;
        }
        for(KouhiDTO kouhi: new KouhiDTO[]{ hoken.kouhi1, hoken.kouhi2, hoken.kouhi3 }) {
            if (kouhi == null) {
                continue;
            }
            int kouhiFutanWari = kouhiFutanWari(kouhi.futansha);
            if (kouhiFutanWari < futanWari) {
                futanWari = kouhiFutanWari;
            }
        }
        return futanWari;
    }

    public static int calcRcptAge(int bdYear, int bdMonth, int bdDay, int atYear, int atMonth){
        int age;
        age = atYear - bdYear;
        if( atMonth < bdMonth ){
            age -= 1;
        } else if( atMonth == bdMonth ){
            if( bdDay != 1 ){
                age -= 1;
            }
        }
        return age;
    }

    public static int calcShahokokuhoFutanWariByAge(int age){
        if( age < 3 )
            return 2;
        else if( age >= 70 )
            return 2;
        else
            return 3;
    };

    public static int kouhiFutanWari(int futanshaBangou){
        if( futanshaBangou / 1000000 == 41 )
            return 1;
        else if( (futanshaBangou / 1000) == 80136 )
            return 1;
        else if( (futanshaBangou / 1000) == 80137 )
            return 0;
        else if( (futanshaBangou / 1000) == 81136 )
            return 1;
        else if( (futanshaBangou / 1000) == 81137 )
            return 0;
        else if( (futanshaBangou / 1000000) == 88 )
            return 0;
        else{
            System.out.println("unknown kouhi futansha: " + futanshaBangou);
            return 0;
        }
    }

}
