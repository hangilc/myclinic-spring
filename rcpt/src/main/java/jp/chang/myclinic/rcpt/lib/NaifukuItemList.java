package jp.chang.myclinic.rcpt.lib;

public class NaifukuItemList<T> extends RcptShuukeiImpl<NaifukuItem<T>> {

    public void extendOrAdd(String usage, int days, int iyakuhincode, double amountTimesYakka, T drug){
        for(NaifukuItem<T> e: getItems()){
            if( e.canExtend(usage, days) ){
                e.extend(iyakuhincode, amountTimesYakka, drug);
                return;
            }
        }
        NaifukuItem<T> item = new NaifukuItem<>(usage, days, iyakuhincode, amountTimesYakka, drug);
        add(item);
    }

}
