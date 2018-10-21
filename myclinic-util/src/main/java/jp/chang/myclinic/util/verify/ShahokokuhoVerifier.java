package jp.chang.myclinic.util.verify;

import jp.chang.myclinic.util.dto_logic.HokenLib;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class ShahokokuhoVerifier extends VerifierBase {

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
        if (!HokenLib.hasValidCheckingDigit(bangou)) {
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

    public static void setHihokenshaKigou(String value, Consumer<String> handler) {
        if (value == null) {
            value = "";
        }
        if (handler != null) {
            handler.accept(value);
        }
    }

    public static void setHihokenshaBangou(String value, Consumer<String> handler) {
        if (value == null) {
            value = "";
        }
        if (handler != null) {
            handler.accept(value);
        }
    }

    public static String verifyHonninKazoku(int value, Consumer<Integer> handler) {
        if (value == 0 || value == 1) {
            if (handler != null) {
                handler.accept(value);
            }
            return null;
        } else {
            return "本人・家族の値が不適切です。";
        }
    }

    public static String verifyKourei(Integer value, Consumer<Integer> handler) {
        if (value != null && value >= 0 && value <= 3) {
            if (handler != null) {
                handler.accept(value);
            }
            return null;
        } else {
            return "高齢の設定が不適切です。";
        }
    }

    public static String verifyHihokenshaKigouAndBangou(String kigou, String bangou) {
        if ((kigou == null || kigou.isEmpty()) && (bangou == null || bangou.isEmpty())) {
            return "被保険者記号と被保険者番号が両方空白です。";
        } else {
            return null;
        }
    }

}
