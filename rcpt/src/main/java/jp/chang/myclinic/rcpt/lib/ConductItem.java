package jp.chang.myclinic.rcpt.lib;

import jp.chang.myclinic.consts.ConductKind;

public class ConductItem<D, K> implements RcptItem, Mergeable<ConductItem<D, K>> {

    private ConductKind kind;
    private ShinryouItemList shinryouList = new ShinryouItemList();
    private ConductDrugItemList<D> drugs = new ConductDrugItemList<>();
    private KizaiItemList<K> kizaiList = new KizaiItemList<>();
    private int count;

    ConductItem(ConductKind kind) {
        this.kind = kind;
        this.count = 1;
    }

    @Override
    public int getTanka() {
        return shinryouList.getTen() + drugs.getTen() + kizaiList.getTen();
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public boolean canMerge(ConductItem<D, K> src) {
        return kind == src.kind && shinryouList.canMerge(src.shinryouList);
    }

    @Override
    public void merge(ConductItem<D, K> src) {
        count += src.count;
    }
}
