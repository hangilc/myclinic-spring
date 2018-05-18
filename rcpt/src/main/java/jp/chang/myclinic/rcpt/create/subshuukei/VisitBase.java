package jp.chang.myclinic.rcpt.create.subshuukei;

import jp.chang.myclinic.rcpt.create.Shinryou;
import jp.chang.myclinic.rcpt.lib.ShinryouItem;
import jp.chang.myclinic.rcpt.lib.ShinryouItemList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

class VisitBase {

    private SubShuukei subShuukei;
    private List<Tekiyou> tekiyouList = new ArrayList<>();

    VisitBase(SubShuukei subShuukei) {
        this.subShuukei = subShuukei;
    }

    void addTekiyou(RcptItem item){
        tekiyouList.add(new Tekiyou(item.name, item.tanka, item.count));
    }

    void addTekiyou(ShinryouItem item){
        tekiyouList.add(new Tekiyou(item.getName(), item.getTensuu(), item.getCount()));
    }

    void outputTekiyou(){
        Tekiyou.output("" + subShuukei.getCode(), tekiyouList);
    }

    ShinryouItem createShinryouItem(Shinryou src){
        return new ShinryouItem(src.getShinryoucode(), src.getName(), src.getTensuu());
    }

    Optional<Integer> getShuukeiTanka(ShinryouItemList shuukei){
        Set<Integer> tankaSet =  shuukei.stream().map(ShinryouItem::getTensuu).collect(Collectors.toSet());
        if( tankaSet.size() == 1 ){
            return Optional.of(tankaSet.iterator().next());
        } else {
            return Optional.empty();
        }
    }

    int getTotalCount(ShinryouItemList shuukei){
        return shuukei.stream().mapToInt(ShinryouItem::getCount).sum();
    }

    void outputShuukei(String prefix, ShinryouItemList shuukei) {
        outputShuukei(prefix, getShuukeiTanka(shuukei).orElse(null),
                getTotalCount(shuukei), shuukei.getTen());
    }

    void outputShuukei(String prefix, ShinryouItemList shuukei, boolean showTanka, boolean showCount){
        if( !shuukei.isEmpty() ){
            Integer tanka = null, count = null, ten = null;
            if( showTanka ){
                tanka = getShuukeiTanka(shuukei).orElse(null);
            }
            if( showCount ) {
                count = getTotalCount(shuukei);
            }
            ten = shuukei.getTen();
            outputShuukei(prefix, tanka, count, ten);
        }
    }

    void outputShuukei(String prefix, Integer tanka, Integer count, Integer ten){
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
