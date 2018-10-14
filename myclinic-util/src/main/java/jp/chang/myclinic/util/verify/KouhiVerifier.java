package jp.chang.myclinic.util.verify;

import java.util.function.Consumer;

public class KouhiVerifier extends VerifierBase{

    //private static Logger logger = LoggerFactory.getLogger(KouhiVerifier.class);

    public KouhiVerifier() {

    }
    public static String verifyFutanshaBangou(int bangou){
        if( bangou <= 9999999 ){
            return "負担者番号の桁数が少なすぎます。";
        } else if( bangou > 99999999 ){
            return "負担者番号の桁数が多すぎます。";
        }
        int rem = bangou % 1000000;
        int todoufuken = rem / 10000;
        if( !HokenVerifierLib.verifyTodoufukenBangou(todoufuken) ){
            return "負担者番号の都道府県番号部分が不適切です。";
        }
        if( !HokenVerifierLib.verifyHokenshaBangou(bangou) ){
            return "負担者番号の検証番号が正しくありません。";
        }
        return null;
    }

    public static String verifyFutanshaBangouInput(String bangouInput, Consumer<Integer> handler){
        if( bangouInput == null || bangouInput.isEmpty() ){
            return "負担者番号が設定されていません。";
        }
        try {
            int bangou = Integer.parseInt(bangouInput);
            String err = verifyFutanshaBangou(bangou);
            if( err == null ){
                if( handler != null ){
                    handler.accept(bangou);
                }
            }
            return err;
        } catch(NumberFormatException ex){
            return "負担者番号が数値でありません。";
        }
    }

    public static String verifyJukyuushaBangou(int bangou){
        if( bangou <= 999999 ){
            return "受給者番号の桁数が少なすぎます。";
        } else if( bangou > 9999999 ){
            return "受給者番号の桁数が多すぎます。";
        }
        if( !HokenVerifierLib.verifyHokenshaBangou(bangou) ){
            return "受給者番号の検証番号が正しくありません。";
        }
        return null;
    }

    public static String verifyJukyuushaBangouInput(String bangouInput, Consumer<Integer> handler){
        if( bangouInput == null || bangouInput.isEmpty() ){
            return "受給者番号が設定されていません。";
        }
        try {
            int bangou = Integer.parseInt(bangouInput);
            String err = verifyJukyuushaBangou(bangou);
            if( err == null ){
                if( handler != null ){
                    handler.accept(bangou);
                }
            }
            return err;
        } catch(NumberFormatException ex){
            return "受給者番号が数値でありません。";
        }
    }

}
