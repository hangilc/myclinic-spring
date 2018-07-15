package jp.chang.myclinic.rcpt.create.subshuukei;

import jp.chang.myclinic.rcpt.create.Globals;

public class ShuukeiMap {

    private ShoshinVisit shoshinVisit = new ShoshinVisit(Globals.shinryouMasterMap);
    private SaishinVisit saishinVisit = new SaishinVisit(Globals.shinryouMasterMap);
    private ShidouVisit shidouVisit = new ShidouVisit(Globals.shinryouMasterMap);
    private ZaitakuVisit zaitakuVisit = new ZaitakuVisit(Globals.shinryouMasterMap);
    private TouyakuVisit touyakuVisit = new TouyakuVisit(Globals.shinryouMasterMap);
    private ChuushaVisit chuushaVisit = new ChuushaVisit();
    private ShochiVisit shochiVisit = new ShochiVisit();
    private ShujutsuVisit shujutsuVisit = new ShujutsuVisit();
    private KensaVisit kensaVisit = new KensaVisit();
    private GazouVisit gazouVisit = new GazouVisit();
    private SonotaVisit sonotaVisit = new SonotaVisit(Globals.shinryouMasterMap);

    public ShoshinVisit getShoshinVisit() {
        return shoshinVisit;
    }

    public SaishinVisit getSaishinVisit() {
        return saishinVisit;
    }

    public ShidouVisit getShidouVisit() {
        return shidouVisit;
    }

    public ZaitakuVisit getZaitakuVisit() {
        return zaitakuVisit;
    }

    public TouyakuVisit getTouyakuVisit() {
        return touyakuVisit;
    }

    public ChuushaVisit getChuushaVisit() {
        return chuushaVisit;
    }

    public ShochiVisit getShochiVisit() {
        return shochiVisit;
    }

    public ShujutsuVisit getShujutsuVisit() {
        return shujutsuVisit;
    }

    public KensaVisit getKensaVisit() {
        return kensaVisit;
    }

    public GazouVisit getGazouVisit() {
        return gazouVisit;
    }

    public SonotaVisit getSonotaVisit() {
        return sonotaVisit;
    }

    public void merge(ShuukeiMap src){
        shoshinVisit.merge(src.shoshinVisit);
        saishinVisit.merge(src.saishinVisit);
        shidouVisit.merge(src.shidouVisit);
        zaitakuVisit.merge(src.zaitakuVisit);
        touyakuVisit.merge(src.touyakuVisit);
        chuushaVisit.merge(src.chuushaVisit);
        shochiVisit.merge(src.shochiVisit);
        shujutsuVisit.merge(src.shujutsuVisit);
        kensaVisit.merge(src.kensaVisit);
        gazouVisit.merge(src.gazouVisit);
        sonotaVisit.merge(src.sonotaVisit);
    }

    public void output(){
        shoshinVisit.output();
        saishinVisit.output();
        shidouVisit.output();
        zaitakuVisit.output();
        touyakuVisit.output();
        chuushaVisit.output();
        shochiVisit.output();
        shujutsuVisit.output();
        kensaVisit.output();
        gazouVisit.output();
        sonotaVisit.output();
        System.out.printf("kyuufu.hoken.seikyuuten %d\n", calcSeikyuuTen());
    }

    public int calcSeikyuuTen(){
        return
        shoshinVisit.getTen() +
        saishinVisit.getTen() +
        shidouVisit.getTen() +
        zaitakuVisit.getTen() +
        touyakuVisit.getTen() +
        chuushaVisit.getTen() +
        shochiVisit.getTen() +
        shujutsuVisit.getTen() +
        kensaVisit.getTen() +
        gazouVisit.getTen() +
        sonotaVisit.getTen();
   }

}
