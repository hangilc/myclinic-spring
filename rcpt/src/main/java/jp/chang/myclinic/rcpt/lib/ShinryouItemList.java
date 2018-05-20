package jp.chang.myclinic.rcpt.lib;

import java.util.function.Consumer;

public class ShinryouItemList extends RcptShuukeiImpl<ShinryouItem> {

    public void forEach(Consumer<ShinryouItem> f){
        getItems().forEach(f);
    }

    public boolean eqv(ShinryouItemList src){

    }
}
