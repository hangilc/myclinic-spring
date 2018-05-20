package jp.chang.myclinic.rcpt.lib;

public class KizaiItemList<T> extends RcptShuukeiImpl<KizaiItem<T>> implements EqvList<KizaiItem<T>> {

    boolean eqv(KizaiItemList<T> src){
        return eqvStream(stream(), src.stream());
    }

}
