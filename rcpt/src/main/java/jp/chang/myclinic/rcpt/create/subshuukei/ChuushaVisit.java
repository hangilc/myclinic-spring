package jp.chang.myclinic.rcpt.create.subshuukei;

import jp.chang.myclinic.consts.ConductKind;
import jp.chang.myclinic.rcpt.create.*;
import jp.chang.myclinic.rcpt.lib.ConductItem;
import jp.chang.myclinic.rcpt.lib.ConductItemList;

import java.util.Collections;
import java.util.List;

import static jp.chang.myclinic.consts.MyclinicConsts.*;

public class ChuushaVisit extends VisitBase {

    private ConductItemList<ConductDrug, ConductKizai> hika = new ConductItemList<>();
    private ConductItemList<ConductDrug, ConductKizai> joumyaku = new ConductItemList<>();
    private ConductItemList<ConductDrug, ConductKizai> sonota = new ConductItemList<>();

    public void add(Conduct conduct){
        ConductKind kind = ConductKind.fromKanjiRep(conduct.kind);
        ConductItem<ConductDrug, ConductKizai> item = new ConductItem<>(kind);
        item.setGazouLabel(conduct.label);
        conduct.shinryouList.forEach(s -> item.add(createShinryouItem(s)));
        conduct.drugs.forEach(d -> item.add(createConductDrugItem(kind, d)));
        conduct.kizaiList.forEach(k -> item.add(createKizaiItem(k)));
    }

    public void add(Shinryou shinryou){
        switch(shinryou.getShuukeisaki()){
            case SHUUKEI_CHUUSHA_HIKA: {
                addSingleShinryou(hika, ConductKind.HikaChuusha, shinryou);
                break;
            }
            case SHUUKEI_CHUUSHA_JOUMYAKU: {
                addSingleShinryou(joumyaku, ConductKind.JoumyakuChuusha, shinryou);
                break;
            }
            case SHUUKEI_CHUUSHA_OTHERS: // fall through
            case SHUUKEI_CHUUSHA_SEIBUTSUETC: {
                addSingleShinryou(sonota, ConductKind.OtherChuusha, shinryou);
                break;
            }
            default: throw new RuntimeException("Unknown shuukeisaki: " + shinryou.getShuukeisaki());
        }
    }

    private void addSingleShinryou(ConductItemList<ConductDrug, ConductKizai> items,
                                   ConductKind kind, Shinryou shinryou){
        Conduct c = new Conduct();
        c.kind = kind.getKanjiRep();
        ConductShinryou cs = new ConductShinryou();
        cs.shinryoucode = shinryou.getShinryoucode();
        cs.name = shinryou.getName();
        cs.tensuu = shinryou.getTensuu();
        c.shinryouList = List.of(cs);
        c.drugs = Collections.emptyList();
        c.kizaiList = Collections.emptyList();
        add(c);
    }

    void merge(ChuushaVisit src){
        hika.merge(src.hika);
        joumyaku.merge(src.joumyaku);
        sonota.merge(src.sonota);
    }

    void output() {

    }
}
