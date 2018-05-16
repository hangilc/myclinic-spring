package jp.chang.myclinic.rcpt.create.subshuukei;

import jp.chang.myclinic.rcpt.RcptUtil;

import java.util.*;

public class NaifukuItem {

    private String usage;
    private int days;
    private Set<Integer> shinryoucodes = new LinkedHashSet<>();
    private List<Double> yakkaList = new ArrayList<>();

    public NaifukuItem(String usage, int days, int shinryoucode, double totalYakka) {
        this.usage = normalizeUsage(usage);
        this.days = days;
        shinryoucodes.add(shinryoucode);
        yakkaList.add(totalYakka);
    }

    private String normalizeUsage(String usage){
        if( usage.equals("就寝前") ){
            return "寝る前";
        } else {
            return usage;
        }
    }

    public boolean canExtend(String usage, int days){
        return Objects.equals(this.usage, normalizeUsage(usage)) && this.days == days;
    }

    public void extend(int shinryoucode, double totalYakka){
        shinryoucodes.add(shinryoucode);
        yakkaList.add(totalYakka);
    }

    public int getTanka(){
        double kingaku = yakkaList.stream().mapToDouble(Double::doubleValue).sum();
        return RcptUtil.touyakuKingakuToTen(kingaku);
    }

    public int getDays(){
        return days;
    }

    public boolean canMerge(NaifukuItem src){
        return Objects.equals(usage, src.usage) && days == src.days &&
                shinryoucodes.equals(src.shinryoucodes);
    }

    public void merge(NaifukuItem src){
        this.days += src.days;
    }

}
