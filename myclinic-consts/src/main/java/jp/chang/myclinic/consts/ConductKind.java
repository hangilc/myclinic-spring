package jp.chang.myclinic.consts;

public enum ConductKind {
    HikaChuusha(0, "皮下・筋肉注射"),
    JoumyakuChuusha(1, "静脈注射"),
    OtherChuusha(2, "その他の注射"),
    Gazou(3, "画像");

    private int code;
    private String kanjiRep;

    ConductKind(int code, String kanjiRep){
        this.code = code;
        this.kanjiRep = kanjiRep;
    }

    public int getCode() {
        return code;
    }

    public String getKanjiRep() {
        return kanjiRep;
    }

    public static ConductKind fromCode(int code){
        for(ConductKind conductKind: values()){
            if( conductKind.code == code ){
                return conductKind;
            }
        }
        return null;
    }

    public static String codeToKanjiRep(int code){
        ConductKind kind = fromCode(code);
        if( kind == null ){
            return "??";
        } else {
            return kind.getKanjiRep();
        }
    }
}
