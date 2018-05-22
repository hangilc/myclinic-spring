package jp.chang.myclinic.rcpt.create.subshuukei;

import jp.chang.myclinic.consts.ConductKind;
import jp.chang.myclinic.consts.HoukatsuKensaKind;
import jp.chang.myclinic.rcpt.create.*;
import jp.chang.myclinic.rcpt.lib.*;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

class VisitBase {

    ShinryouItem createShinryouItem(Shinryou src){
        return new ShinryouItem(src.getShinryoucode(), src.getName(), src.getTensuu());
    }

    ShinryouItem createShinryouItem(ConductShinryou src){
        return new ShinryouItem(src.shinryoucode, src.name, src.tensuu);
    }

    ConductDrugItem<ConductDrug> createConductDrugItem(ConductKind kind, ConductDrug src){
        return new ConductDrugItem<>(kind, src.iyakuhincode, src.amount, src.yakka, src);
    }

    KizaiItem<ConductKizai> createKizaiItem(ConductKizai src){
        return new KizaiItem<>(src.kizaicode, src.amount, src.kingaku, src);
    }

    HoukatsuKensaItem<Shinryou> createHoukatsuKensaItem(HoukatsuKensaKind kind, Shinryou shinryou){
        return new HoukatsuKensaItem<>(kind, Globals.at, shinryou.getShinryoucode(), shinryou.getTensuu(), shinryou);
    }

    ConductItem<ConductDrug, ConductKizai> createConductItem(Conduct conduct){
        ConductKind kind = ConductKind.fromKanjiRep(conduct.kind);
        ConductItem<ConductDrug, ConductKizai> item = new ConductItem<>(kind);
        item.setGazouLabel(conduct.label);
        conduct.shinryouList.forEach(s -> item.add(createShinryouItem(s)));
        conduct.drugs.forEach(d -> item.add(createConductDrugItem(kind, d)));
        conduct.kizaiList.forEach(k -> item.add(createKizaiItem(k)));
        return item;
    }

    Optional<Integer> getShuukeiTanka(RcptShuukei<? extends RcptItem> shuukei){
        Set<Integer> tankaSet =  shuukei.stream().map(RcptItem::getTanka).collect(Collectors.toSet());
        if( tankaSet.size() == 1 ){
            return Optional.of(tankaSet.iterator().next());
        } else {
            return Optional.empty();
        }
    }

    void outputShuukei(String prefix, RcptShuukei<? extends RcptItem> shuukei) {
        outputShuukei(prefix, shuukei, true, true);
    }

    void outputShuukei(String prefix, RcptShuukei<? extends RcptItem> shuukei, boolean showTanka, boolean showCount){
        if( !shuukei.isEmpty() ){
            Integer tanka = null, count = null, ten;
            if( showTanka ){
                tanka = getShuukeiTanka(shuukei).orElse(null);
            }
            if( showCount ) {
                count = shuukei.getTotalCount();
            }
            ten = shuukei.getTen();
            outputShuukei(prefix, tanka, count, ten);
        }
    }

    void outputShuukei(String prefix, Integer tanka, Integer count, Integer ten){
        if( ten != null && ten == 0 ){
            return;
        }
        if( tanka != null ){
            System.out.printf("%s.tanka %d\n", prefix, tanka);
        }
        if( count != null ){
            System.out.printf("%s.kai %d\n", prefix, count);
        }
        if( ten != null ){
            System.out.printf("%s.ten %d\n", prefix, ten);
        }
    }

}
