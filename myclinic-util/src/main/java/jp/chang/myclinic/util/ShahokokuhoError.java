package jp.chang.myclinic.util;

public enum ShahokokuhoError {
    HokenshaBangouIsEmpty("保険者番号が指定されていません。"),
    HokenshaBangouIsNotNumber("保険者番号が数字でありません。"),
    HokenshaBangouIsNotPositive("保険者番号が正の数字でありません。"),
    SeikanKenpo("保険者番号が旧政管健保のものです。"),
    HokenshaBangouHasInvalidVerificationDigit("保険者番号が不適切です。"),
    HokenshaBangouHasTooFewDigits("保険者番号の桁数が少なすぎます"),
    HokenshaBangouHasTooManyDigits("保険者番号の桁数が多すぎます。")
    ;

    private String message;

    ShahokokuhoError(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
