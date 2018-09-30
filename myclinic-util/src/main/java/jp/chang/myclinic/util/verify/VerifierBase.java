package jp.chang.myclinic.util.verify;

import java.time.LocalDate;
import java.util.function.Consumer;

class VerifierBase {

    //private static Logger logger = LoggerFactory.getLogger(VerifierBase.class);

    VerifierBase() {

    }

    public static String verifyValidFrom(LocalDate value, Consumer<String> handler) {
        if (value == null || value == LocalDate.MAX) {
            return "資格取得日が不適切です。";
        } else {
            if (handler != null) {
                handler.accept(value.toString());
            }
            return null;
        }
    }

    public static String verifyValidUpto(LocalDate value, Consumer<String> handler) {
        if (value == null) {
            return "有効期限が不適切です。";
        } else if (value == LocalDate.MAX) {
            if (handler != null) {
                handler.accept("0000-00-00");
            }
            return null;
        } else {
            if (handler != null) {
                handler.accept(value.toString());
            }
            return null;
        }
    }

    public static String verifyValidFromAndValidUpto(String validFrom, String validUpto) {
        if (!validUpto.equals("0000-00-00")) {
            if (validFrom.compareTo(validUpto) > 0) {
                return "資格取得日が有効期限よりも前に日付になっています。";
            }
        }
        return null;
    }

}
