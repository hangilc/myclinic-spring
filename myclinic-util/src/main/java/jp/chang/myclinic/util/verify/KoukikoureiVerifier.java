package jp.chang.myclinic.util.verify;

import java.util.function.Consumer;

public class KoukikoureiVerifier {

    //private static Logger logger = LoggerFactory.getLogger(KoukikoureiVerifier.class);

    private KoukikoureiVerifier() {

    }

    public static String verifyHokenshaBangou(int bangou){
        if( bangou <= 9999999 ){
            return "保険者番号の桁数が少なすぎます。";
        } else if( bangou > 99999999 ){
            return "保険者番号の桁数が多すぎます。";
        }
        int housei = bangou / 1000000;
        if( housei != 39 ){
            return "保険者番号の法別番号部分が不適切です。";
        }
        if( !HokenVerifierLib.verifyHokenshaBangou(bangou) ){
            return "保険者番号の検証番号が正しくありません。";
        }
        return null;
    }

    public static String verifyHokenshaBangouInput(String bangouInput, Consumer<Integer>handler){
        if( bangouInput == null || bangouInput.isEmpty() ){
            return "保険者番号が設定されていません。";
        }
        try {
            int bangou = Integer.parseInt(bangouInput);
            String err = verifyHokenshaBangou(bangou);
            if( err == null ){
                if( handler != null ){
                    handler.accept(bangou);
                }
            }
            return err;
        } catch(NumberFormatException ex){
            return "保険者番号が数値でありません。";
        }
    }

}
