package jp.chang.myclinic.rcpt.lib;

import jp.chang.myclinic.consts.HoukatsuKensaKind;

public class HoukatsuKensaItemList<T> extends RcptShuukeiImpl<HoukatsuKensaItem<T>> {

    public void extendOrAdd(HoukatsuKensaKind kind,int shinryoucode, int tensuu, T shinryou){
        for(HoukatsuKensaItem<T> e: getItems()){
            if( e.canExtend(kind) ){
                e.extend(shinryoucode, tensuu, shinryou);
                return;
            }
        }
        HoukatsuKensaItem<T> item = new HoukatsuKensaItem<>(kind, shinryoucode, tensuu, shinryou);
        add(item);
    }

}
