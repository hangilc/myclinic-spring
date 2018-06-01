package jp.chang.myclinic.rcpt.create.subshuukei;

import jp.chang.myclinic.consts.HoukatsuKensaKind;
import jp.chang.myclinic.mastermap.generated.ResolvedShinryouMap;
import jp.chang.myclinic.rcpt.create.Globals;
import jp.chang.myclinic.rcpt.create.Shinryou;
import jp.chang.myclinic.rcpt.lib.HoukatsuKensaItem;
import jp.chang.myclinic.rcpt.lib.HoukatsuKensaItemList;
import jp.chang.myclinic.rcpt.lib.ShinryouItemList;

import java.util.stream.Collectors;

public class KensaVisit extends VisitBase {

    private HoukatsuKensaItemList<ShinryouItemData> houkatsuList = new HoukatsuKensaItemList<>();
    private ShinryouItemList<ShinryouItemData> handanryouList = new ShinryouItemList<>();
    private ShinryouItemList<ShinryouItemData> shinryouList = new ShinryouItemList<>();

    public void add(Shinryou shinryou){
        HoukatsuKensaKind kind = HoukatsuKensaKind.fromCode(shinryou.getHoukatsuKensa());
        if( kind != null && kind != HoukatsuKensaKind.NONE ){
            HoukatsuKensaItem<ShinryouItemData> item = new HoukatsuKensaItem<>(kind, Globals.at,
                    shinryou.getShinryoucode(), shinryou.getTensuu(),
                    new ShinryouItemData(shinryou.getName()));
            houkatsuList.extendOrAdd(item);
        } else if( isHandanryou(shinryou.getShinryoucode()) ) {
            int shinryoucode = shinryou.getShinryoucode();
            if( handanryouList.stream().anyMatch(item -> item.getShinryoucode() == shinryoucode) ){
                throw new RuntimeException("Duplicate handanryou.");
            }
            handanryouList.add(createShinryouItem(shinryou));
        } else {
            shinryouList.add(createShinryouItem(shinryou));
        }
    }

    private boolean isHandanryou(int shinryoucode){
        ResolvedShinryouMap map = Globals.shinryouMasterMap;
        return shinryoucode == map.尿便検査判断料 ||
                shinryoucode == map.血液検査判断料 ||
                shinryoucode == map.生化Ⅰ判断料 ||
                shinryoucode == map.生化Ⅱ判断料 ||
                shinryoucode == map.免疫検査判断料 ||
                shinryoucode == map.微生物検査判断料 ||
                shinryoucode == map.病理判断料;
    }

    private String rewriteHandanryouName(int shinryoucode){
        ResolvedShinryouMap map = Globals.shinryouMasterMap;
        if( shinryoucode == map.尿便検査判断料 ){
            return "尿";
        } else if( shinryoucode == map.血液検査判断料 ){
            return "血";
        } else if( shinryoucode == map.生化Ⅰ判断料 ){
            return "生Ⅰ";
        } else if( shinryoucode == map.生化Ⅱ判断料 ){
            return "生Ⅱ";
        } else if( shinryoucode == map.免疫検査判断料 ){
            return "免";
        } else if( shinryoucode == map.微生物検査判断料 ){
            return "微";
        } else if( shinryoucode == map.病理判断料 ){
            return "病学";
        } else {
            throw new RuntimeException("Invalid hanndanryou: " + shinryoucode);
        }
    }

    void merge(KensaVisit src){
        houkatsuList.merge(src.houkatsuList);
        handanryouList.merge(src.handanryouList);
        shinryouList.merge(src.shinryouList);
    }

    void output(){
        // handanryou is grouped to one tekiyou (together counted as 1)
        int count = houkatsuList.getTotalCount() +
                (handanryouList.isEmpty() ? 0 : 1) +
                shinryouList.getTotalCount();
        int ten = houkatsuList.getTen() + handanryouList.getTen() +
                shinryouList.getTen();
        if( ten > 0 ){
            outputShuukei("kensa", null, count, ten);
        }
        TekiyouList tekiyouList = new TekiyouList(SubShuukei.SUB_KENSA);
        houkatsuList.stream().forEach(houkatsu -> {
            String label = houkatsuLabel(houkatsu);
            tekiyouList.add(label, houkatsu.getTanka(), houkatsu.getCount());
        });
        if( !handanryouList.isEmpty() ) {
            tekiyouList.add(handanryouLabel(), handanryouList.getTen(), 1);
        }
        shinryouList.stream().forEach(tekiyouList::add);
        tekiyouList.output();
    }

    private String houkatsuLabel(HoukatsuKensaItem<ShinryouItemData> item){
        return item.getDataList().stream()
                .map(ShinryouItemData::getName).collect(Collectors.joining("、"));
    }

    private String handanryouLabel(){
        String names = handanryouList.stream().map(s -> rewriteHandanryouName(s.getShinryoucode()))
                .collect(Collectors.joining("、"));
        return "（判）" + names;
    }

    int getTen(){
        return houkatsuList.getTen() + handanryouList.getTen() + shinryouList.getTen();
    }

}
