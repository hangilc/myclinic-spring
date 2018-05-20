package jp.chang.myclinic.rcpt.lib;

import java.util.function.Consumer;

public class ShinryouItemList extends RcptShuukeiImpl<ShinryouItem> implements EqvList<ShinryouItem> {

    public void forEach(Consumer<ShinryouItem> f){
        getItems().forEach(f);
    }

    boolean eqv(ShinryouItemList src){
        return eqvStream(stream(), src.stream());
    }

}
