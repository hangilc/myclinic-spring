package jp.chang.myclinic.util;

public enum VerifyShahokokuhoBangou {

    OK("正しい値です。"),
    EMPTY("入力されていません。"),
    INVALLID_NUMBER("数字でありません。"),
    SEIKANKENPO("政管健保の値です。"),
    TOO_MANY_DIGITS("桁数が多すぎます。"),
    TOO_FEW_DIGITS("桁数が少なすぎます。"),
    VERIFY_ERROR("検証番号が一致しません。");

    private String message;

    VerifyShahokokuhoBangou(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
