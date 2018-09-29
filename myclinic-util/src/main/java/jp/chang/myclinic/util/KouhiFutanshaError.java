package jp.chang.myclinic.util;

public enum KouhiFutanshaError {

    HokenshaBangouIsEmpty("公費負担者番号が指定されていません。"),
    HokenshaBangouIsNotNumber("公費負担者番号が数字でありません。"),
    HokenshaBangouIsNotPositive("公費負担者番号が正の数字でありません。"),
    HokenshaBangouHasInvalidVerificationDigit("公費負担者番号が不適切です。"),
    HokenshaBangouHasTooFewDigits("公費負担者番号の桁数が少なすぎます"),
    HokenshaBangouHasTooManyDigits("公費負担者番号の桁数が多すぎます。"),
    HouseiBangouIsInvalid("公費負担者番号の法制番号部分が不適切です。"),
    TodoufukenBangouIsInvalid("公費負担者番号の都道府県番号部分が不適切です。")
    ;

    private String message;

    KouhiFutanshaError(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
