package jp.chang.myclinic.consts;

/**
 * Created by hangil on 2017/06/04.
 */
public enum PharmaQueueState {
    WaitPack(0),
    InPack(1),
    PackDone(2);

    private int code;

    PharmaQueueState(int code){
        this.code = code;
    }

    public int getCode(){
        return code;
    }
}
