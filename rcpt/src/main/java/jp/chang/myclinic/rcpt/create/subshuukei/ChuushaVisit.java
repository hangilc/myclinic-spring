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
        ConductItem<ConductDrug, ConductKizai> item = createConductItem(conduct);
        ConductKind kind = ConductKind.fromKanjiRep(conduct.kind);
        if( kind == null ){
            throw new RuntimeException("Unknown conduct kind: " + conduct.kind);
        }
        switch(kind){
            case HikaChuusha: hika.add(item); break;
            case JoumyakuChuusha: joumyaku.add(item); break;
            case OtherChuusha: sonota.add(item); break;
            default: throw new RuntimeException("Unkonw conduct kind: " + kind);
        }
    }

    public void add(Shinryou shinryou){
        switch(shinryou.getShuukeisaki()){
            case SHUUKEI_CHUUSHA_HIKA: {
                addSingleShinryou(ConductKind.HikaChuusha, shinryou);
                break;
            }
            case SHUUKEI_CHUUSHA_JOUMYAKU: {
                addSingleShinryou(ConductKind.JoumyakuChuusha, shinryou);
                break;
            }
            case SHUUKEI_CHUUSHA_OTHERS: // fall through
            case SHUUKEI_CHUUSHA_SEIBUTSUETC: {
                addSingleShinryou(ConductKind.OtherChuusha, shinryou);
                break;
            }
            default: throw new RuntimeException("Unknown shuukeisaki: " + shinryou.getShuukeisaki());
        }
    }

    private void addSingleShinryou(ConductKind kind, Shinryou shinryou){
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
        outputShuukei("chuusha.hika", null, hika.getTotalCount(), hika.getTen());
        outputShuukei("chuusha.joumyaku", null, joumyaku.getTotalCount(), joumyaku.getTen());
        outputShuukei("chuusha.sonota", null, sonota.getTotalCount(), sonota.getTen());
        outputTekiyou(SubShuukeiChuusha.CHUUSHA_HIKA, hika);
        outputTekiyou(SubShuukeiChuusha.CHUUSHA_JOUMYAKU, joumyaku);
        outputTekiyou(SubShuukeiChuusha.CHUUSHA_SONOTA, sonota);
    }

    private void outputTekiyou(SubShuukeiChuusha shuukei, ConductItemList<ConductDrug, ConductKizai> items){
        TekiyouList tekiyouList = new TekiyouList(shuukei);
        items.stream().forEach(tekiyouList::add);
        tekiyouList.output();
    }

    int getTen(){
        return hika.getTen() + joumyaku.getTen() + sonota.getTen();
    }

}
