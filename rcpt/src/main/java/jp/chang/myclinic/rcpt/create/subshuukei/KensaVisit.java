package jp.chang.myclinic.rcpt.create.subshuukei;

import jp.chang.myclinic.consts.HoukatsuKensaKind;
import jp.chang.myclinic.mastermap.generated.ResolvedShinryouMap;
import jp.chang.myclinic.rcpt.create.Globals;
import jp.chang.myclinic.rcpt.create.Shinryou;
import jp.chang.myclinic.rcpt.lib.HoukatsuKensaItemList;
import jp.chang.myclinic.rcpt.lib.ShinryouItemList;

public class KensaVisit extends VisitBase {

    private HoukatsuKensaItemList<Shinryou> houkatsuList = new HoukatsuKensaItemList<>();
    private ShinryouItemList handanryouList = new ShinryouItemList();
    private ShinryouItemList shinryouList = new ShinryouItemList();

    public void add(Shinryou shinryou){
        HoukatsuKensaKind kind = HoukatsuKensaKind.fromCode(shinryou.getKensaGroup());
        if( kind != null && kind != HoukatsuKensaKind.NONE ){
            houkatsuList.extendOrAdd(kind, Globals.at, shinryou.getShinryoucode(), shinryou.getTensuu(),
                    shinryou);
        } else {

        }
    }

    private boolean isHandanryou(int shinryoucode){
        ResolvedShinryouMap map = Globals.shinryouMasterMap;
        return shinryoucode == map.尿便検査判断料 ||
                shinryoucode == map.血液検査判断料 ||
                shinryoucode == map.生化Ⅰ判断料 ||
                shinryoucode == map.生化Ⅱ判断料 ||
                shinryoucode == map.血液検査判断料 ||
                shinryoucode == map.血液検査判断料 ||
                shinryoucode == map.血液検査判断料 ||
    }

}
