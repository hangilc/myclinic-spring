package jp.chang.myclinic.rcpt.lib;

public class ConductDrugItemList<T> extends RcptShuukeiImpl<ConductDrugItem<T>>
        implements EqvList<ConductDrugItem<T>> {

    boolean eqv(ConductDrugItemList<T> src){
        return eqvStream(stream(), src.stream());
    }

}
