package jp.chang.myclinic.rcpt.create.subshuukei;

import jp.chang.myclinic.rcpt.create.Shinryou;
import jp.chang.myclinic.rcpt.lib.ShinryouItem;

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

    Optional<Integer> getShuukeiTanka(RcptItemMap map){
        Set<Integer> tankaSet =  map.values().stream().map(item -> item.tanka).collect(Collectors.toSet());
        if( tankaSet.size() == 1 ){
            return Optional.of(tankaSet.iterator().next());
        } else {
            return Optional.empty();
        }
    }

    int getTotalCount(RcptItemMap map){
        return map.values().stream().mapToInt(item -> item.count).sum();
    }

    int getTotalTen(RcptItemMap map){
        return map.values().stream().mapToInt(RcptItem::getTen).sum();
    }

    void outputShuukei(String prefix, RcptItemMap shuukei){
        outputShuukei(prefix, shuukei, true, true);
    }

    void outputShuukei(String prefix, RcptItemMap shuukei, boolean showTanka, boolean showCount){
        if( !shuukei.isEmpty() ){
            getShuukeiTanka(shuukei).ifPresent(tanka ->
                System.out.printf("%s.tanka %d\n", prefix, tanka));
            if( showTanka ) {
                System.out.printf("%s.kai %d\n", prefix, getTotalCount(shuukei));
            }
            if( showCount ) {
                System.out.printf("%s.ten %d\n", prefix, getTotalTen(shuukei));
            }
        }
    }

}
