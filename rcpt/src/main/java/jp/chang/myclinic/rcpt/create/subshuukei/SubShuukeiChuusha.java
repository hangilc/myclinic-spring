package jp.chang.myclinic.rcpt.create.subshuukei;

public enum SubShuukeiChuusha {
    CHUUSHA_HIKA(31),
    CHUUSHA_JOUMYAKU(32),
    CHUUSHA_SONOTA(33);

    private int code;

    SubShuukeiChuusha(int code){
        this.code = code;
    }

    public int getCode(){
        return code;
    }
}
