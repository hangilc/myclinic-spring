package jp.chang.myclinic.util;

public enum KoukikoureiError {

    HokenshaBangouIsEmpty("保険者番号が指定されていません。"),
    HokenshaBangouIsNotNumber("保険者番号が数字でありません。"),
    HokenshaBangouIsNotPositive("保険者番号が正の数字でありません。"),
    HokenshaBangouHasInvalidVerificationDigit("保険者番号が不適切です。"),
    HokenshaBangouHasTooFewDigits("保険者番号の桁数が少なすぎます"),
    HokenshaBangouHasTooManyDigits("保険者番号の桁数が多すぎます。"),
    HouseiBangouIsInvalid("保険者番号の法制番号部分が不適切です。");
    ;

    private String message;

    KoukikoureiError(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
