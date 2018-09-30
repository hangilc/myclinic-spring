package jp.chang.myclinic.util.verify;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class ShahokokuhoVerifier {

    private static Logger logger = LoggerFactory.getLogger(ShahokokuhoVerifier.class);

    private ShahokokuhoVerifier() {

    }

    public static String verifyHokenshaBangou(int bangou) {
        if (bangou <= 0) {
            return "保険者番号が正の数値でありません。";
        }
        if (bangou <= 999) {
            return "保険者番号の桁数が少なすぎます。";
        }
        if (bangou <= 9999) {
            return "保険者番号が旧政管健保のものと思われます。";
        }
        if (bangou > 100000000) {
            return "保険者番号の桁数が多すぎます。";
        }
        if (!HokenVerifierLib.verifyHokenshaBangou(bangou)) {
            return "保険者番号の検証番号が正しくありません。";
        }
        return null;
    }

    public static String verifyHokenshaBangouInput(String bangouInput, Consumer<Integer> handler) {
        if( bangouInput == null || bangouInput.isEmpty() ){
            return "保険者番号が設定されていません。";
        }
        try {
            int bangou = Integer.parseInt(bangouInput);
            String err = verifyHokenshaBangou(bangou);
            if( err == null ){
                if( handler != null ) {
                    handler.accept(bangou);
                }
            }
            return err;
        } catch(NumberFormatException ex){
            return "保険者番号が数値でありません。";
        }
    }

}
