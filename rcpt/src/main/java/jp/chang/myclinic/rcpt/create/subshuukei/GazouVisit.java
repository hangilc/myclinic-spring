package jp.chang.myclinic.rcpt.create.subshuukei;

import jp.chang.myclinic.rcpt.create.Conduct;
import jp.chang.myclinic.rcpt.create.ConductDrug;
import jp.chang.myclinic.rcpt.create.ConductKizai;
import jp.chang.myclinic.rcpt.create.Shinryou;
import jp.chang.myclinic.rcpt.lib.ConductItem;
import jp.chang.myclinic.rcpt.lib.ConductItemList;
import jp.chang.myclinic.rcpt.lib.ShinryouItemList;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GazouVisit extends VisitBase {

    private ShinryouItemList<ShinryouItemData> shinryouList = new ShinryouItemList<>();
    private ConductItemList<ShinryouItemData, ConductDrug, ConductKizai> conducts = new ConductItemList<>();

    public void add(Shinryou shinryou) {
        shinryouList.add(createShinryouItem(shinryou));
    }

    public void add(Conduct conduct) {
        conducts.add(createConductItem(conduct));
    }

    void merge(GazouVisit src) {
        shinryouList.merge(src.shinryouList);
        conducts.merge(src.conducts);
    }

    void output() {
        int count = shinryouList.getTotalCount() + conducts.getTotalCount();
        int ten = shinryouList.getTen() + conducts.getTen();
        outputShuukei("gazou", null, count, ten);
        TekiyouList tekiyouList = new TekiyouList(SubShuukei.SUB_GAZOU);
        shinryouList.stream().forEach(tekiyouList::add);
        conducts.stream().forEach(conduct -> {
            String gazouLabel = conduct.getGazouLabel();
            String label = gazouLabel + "（";
            if ("胸部単純Ｘ線".equals(gazouLabel)) {
                label += collectKizaiLabel(conduct).collect(Collectors.joining("、"));
            } else {
                label += collectLabels(conduct).collect(Collectors.joining("、"));
            }
            label += "）";
            tekiyouList.add(label, conduct.getTanka(), conduct.getCount());
        });
        tekiyouList.output();
    }

    private Stream<String> collectKizaiLabel(ConductItem<ShinryouItemData, ConductDrug, ConductKizai> item) {
        return item.getKizaiStream().map(c -> c.getData().name);
    }

    private Stream<String> collectLabels(ConductItem<ShinryouItemData, ConductDrug, ConductKizai> item) {
        return Stream.concat(Stream.concat(
                item.getShinryouStream().map(s -> s.getData().getName()),
                item.getDrugStream().map(c -> c.getData().name)
                )
                , collectKizaiLabel(item));
    }

    int getTen(){
        return shinryouList.getTen() + conducts.getTen();
    }

}
