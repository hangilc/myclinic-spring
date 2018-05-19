package jp.chang.myclinic.rcpt.create.subshuukei;

public enum SubShuukeiTouyaku {

    TouyakuNaifuku(21),
    TouyakuTonpuku(22),
    TouyakuGaiyou(23),
    TouyakuShohou(25);

    private int code;

    SubShuukeiTouyaku(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
