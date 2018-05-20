package jp.chang.myclinic.rcpt.lib;

import jp.chang.myclinic.consts.HoukatsuKensaKind;

import java.time.LocalDate;
import java.util.*;

public class HoukatsuKensaItem<T> implements RcptItem, Mergeable<HoukatsuKensaItem<T>> {

    private static HoukatsuKensa houkatsuDb = HoukatsuKensa.load();
    private static LocalDate at;

    private HoukatsuKensaKind kind;
    private Set<Integer> shinryoucodes = new HashSet<>();
    private int tanka = 0;
    private int count = 1;
    private List<T> shinryouList = new ArrayList<>();

    public HoukatsuKensaItem(HoukatsuKensaKind kind, int shinryoucode, int tanka, T shinryou) {
        this.kind = kind;
        this.shinryoucodes.add(shinryoucode);
        this.tanka += tanka;
        this.shinryouList.add(shinryou);
    }

    public static void setAt(LocalDate atValue){
        at = atValue;
    }

    public boolean canExtend(HoukatsuKensaKind kind){
        return this.kind == kind;
    }

    public void extend(int shinryoucode, int tanka, T shinryou){
        this.shinryoucodes.add(shinryoucode);
        this.tanka += tanka;
        this.shinryouList.add(shinryou);
    }

    @Override
    public boolean canMerge(HoukatsuKensaItem<T> src) {
        return kind == src.kind && shinryoucodes.equals(src.shinryoucodes);
    }

    @Override
    public void merge(HoukatsuKensaItem<T> src) {
        count += src.count;
    }

    @Override
    public int getTanka() {
        return houkatsuDb.calcTen(kind, shinryoucodes.size(), at).orElse(tanka);
    }

    @Override
    public int getCount() {
        return count;
    }

    public List<T> getShinryouList() {
        return shinryouList;
    }
}
