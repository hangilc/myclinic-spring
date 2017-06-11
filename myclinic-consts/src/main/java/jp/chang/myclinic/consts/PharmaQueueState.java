package jp.chang.myclinic.consts;

/**
 * Created by hangil on 2017/06/04.
 */
public enum PharmaQueueState {
    WaitPack(MyclinicConsts.PharmaQueueStateWaitPack),
    InPack(MyclinicConsts.PharmaQueueStateInPack),
    PackDone(MyclinicConsts.PharmaQueueStatePackDone);

    private int code;

    PharmaQueueState(int code){
        this.code = code;
    }

    public int getCode(){
        return code;
    }

    public static PharmaQueueState fromCode(int code){
        for(PharmaQueueState state: values()){
            if( state.code == code ){
                return state;
            }
        }
        return null;
    }
}
