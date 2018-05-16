package jp.chang.myclinic.rcpt.create.subshuukei;

public enum SubShuukei {
    SUB_SHOSHIN(11),
    SUB_SAISHIN(12),
    SUB_SHIDOU(13),
    SUB_ZAITAKU(14),
    SUB_TOUYAKU(20),
    SUB_CHUUSHA(30),
    SUB_SHOCHI(40),
    SUB_SHUJUTSU(50),
    SUB_KENSA(60),
    SUB_GAZOU(70),
    SUB_SONOTA(80);

    private int code;

    SubShuukei(int code){
        this.code = code;
    }

    int getCode(){
        return code;
    }
}
