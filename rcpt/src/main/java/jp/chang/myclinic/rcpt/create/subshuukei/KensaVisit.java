package jp.chang.myclinic.rcpt.create.subshuukei;

import jp.chang.myclinic.consts.HoukatsuKensaKind;
import jp.chang.myclinic.rcpt.create.Shinryou;
import jp.chang.myclinic.rcpt.lib.HoukatsuKensaItemList;
import jp.chang.myclinic.rcpt.lib.ShinryouItemList;

public class KensaVisit extends VisitBase {

    private HoukatsuKensaItemList<Shinryou> houkatsuList = new HoukatsuKensaItemList<>();
    private ShinryouItemList handanryouList = new ShinryouItemList();
    private ShinryouItemList shinryouList = new ShinryouItemList();

    public void add(Shinryou shinryou){
        HoukatsuKensaKind houkatsuKind = HoukatsuKensaKind.fromCode(shinryou.getKensaGroup());
        if( houkatsuKind != null && houkatsuKind != HoukatsuKensaKind.NONE ){
            houkatsuList.add(createHoukatsuKensaItem(houkatsuKind, shinryou));
        } else {

        }
    }

}
