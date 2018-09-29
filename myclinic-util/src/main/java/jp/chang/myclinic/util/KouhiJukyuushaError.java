package jp.chang.myclinic.util;

public enum KouhiJukyuushaError {

    HokenshaBangouIsEmpty("公費受給者番号が指定されていません。"),
    HokenshaBangouIsNotNumber("公費受給者番号が数字でありません。"),
    HokenshaBangouIsNotPositive("公費受給者番号が正の数字でありません。"),
    HokenshaBangouHasInvalidVerificationDigit("公費受給者番号が不適切です。"),
    HokenshaBangouHasTooFewDigits("公費受給者番号の桁数が少なすぎます"),
    HokenshaBangouHasTooManyDigits("公費受給者番号の桁数が多すぎます。"),
    ;

    private String message;

    KouhiJukyuushaError(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
