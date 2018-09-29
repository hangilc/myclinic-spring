package jp.chang.myclinic.util;

public enum KoukikoureiError {

    HokenshaBangouIsEmpty("保険者番号が指定されていません。"),
    HokenshaBangouIsNotNumber("保険者番号が数字でありません。"),
    HokenshaBangouIsNotPositive("保険者番号が正の数字でありません。"),
    HokenshaBangouHasInvalidVerificationDigit("保険者番号が不適切です。"),
    HokenshaBangouHasTooFewDigits("桁数が少なすぎます"),
    HokenshaBangouHasTooManyDigits("桁数が多すぎます。"),
    HouseiBangouIsInvalid("法制番号が不適切です。");
    ;

    private String message;

    KoukikoureiError(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
