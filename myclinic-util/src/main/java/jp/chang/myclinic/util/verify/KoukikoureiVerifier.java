package jp.chang.myclinic.util.verify;

import java.util.function.Consumer;

public class KoukikoureiVerifier extends VerifierBase {

    //private static Logger logger = LoggerFactory.getLogger(KoukikoureiVerifier.class);

    private KoukikoureiVerifier() {

    }

    public static String verifyHokenshaBangou(int bangou) {
        if (bangou <= 9999999) {
            return "保険者番号の桁数が少なすぎます。";
        } else if (bangou > 99999999) {
            return "保険者番号の桁数が多すぎます。";
        }
        int housei = bangou / 1000000;
        if (housei != 39) {
            return "保険者番号の法別番号部分が不適切です。";
        }
        if (!HokenVerifierLib.verifyHokenshaBangou(bangou)) {
            return "保険者番号の検証番号が正しくありません。";
        }
        return null;
    }

    public static String verifyHokenshaBangouInput(String bangouInput, Consumer<Integer> handler) {
        if (bangouInput == null || bangouInput.isEmpty()) {
            return "保険者番号が設定されていません。";
        }
        try {
            int bangou = Integer.parseInt(bangouInput);
            String err = verifyHokenshaBangou(bangou);
            if (err == null) {
                if (handler != null) {
                    handler.accept(bangou);
                }
            }
            return err;
        } catch (NumberFormatException ex) {
            return "保険者番号が数値でありません。";
        }
    }

    public static String verifyHokenshaBangouInputWithOutputString(String bangouInput, Consumer<String> handler) {
        return verifyHokenshaBangouInput(bangouInput, bangou -> {
            if( handler != null ){
                handler.accept(bangouInput);
            }
        });
    }

    public static String verifyHihokenshaBangou(String src, Consumer<Integer> handler){
        if( src == null || src.isEmpty() ){
            return "被保険者番号が設定されていません。";
        }
        try {
            int bangou = Integer.parseInt(src);
            if( bangou <= 0 ){
                return "被保険者番号が正の数値でありません。";
            } else if( bangou >= 100000000 ){
                return "被保険者番号の桁数が多すぎます。";
            } else {
                if (handler != null) {
                    handler.accept(bangou);
                }
                return null;
            }
        } catch(NumberFormatException ex){
            return "被保険者番号が数値でありません。";
        }
    }

    public static String verifyHihokenshaBangouWithOutputString(String src, Consumer<String> handler){
        return verifyHihokenshaBangou(src, bangou -> {
            if( handler != null ){
                String output = String.format("%08d", bangou);
                handler.accept(output);
            }
        });
    }

    public static String verifyFutanWari(int value, Consumer<Integer> handler){
        if( value >= 1 && value <= 3 ){
            if( handler != null ){
                handler.accept(value);
            }
            return null;
        } else {
            return "負担割の値が不適切です。";
        }
    }

}
