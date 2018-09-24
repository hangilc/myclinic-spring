package jp.chang.myclinic.util;

public enum HokenshaBangouAnalysisResult {
    OK("正しい値です。"),
    SEIKANKENPO("政管健保です。"),
    TOO_MANY_DIGITS("桁数が多すぎます。"),
    TOO_FEW_DIGITS("桁数が少なすぎます。"),
    VERIFY_ERROR("検証番号が一致しません。");

    private String message;

    HokenshaBangouAnalysisResult(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
