package jp.chang.myclinic.rcpt.lib;

import jp.chang.myclinic.consts.HoukatsuKensaKind;

import java.time.LocalDate;

public class HoukatsuKensaItemList<T> extends RcptShuukeiImpl<HoukatsuKensaItem<T>> {

    public void extendOrAdd(HoukatsuKensaKind kind, LocalDate at, int shinryoucode, int tensuu, T shinryou){
        for(HoukatsuKensaItem<T> e: getItems()){
            if( e.canExtend(kind) ){
                e.extend(shinryoucode, tensuu, shinryou);
                return;
            }
        }
        HoukatsuKensaItem<T> item = new HoukatsuKensaItem<>(kind, at, shinryoucode, tensuu, shinryou);
        add(item);
    }

}
