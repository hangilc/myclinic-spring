package jp.chang.myclinic.rcpt.newcreate.bill;

import jp.chang.myclinic.rcpt.newcreate.input.Naifuku;
import jp.chang.myclinic.util.RcptUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

class NaifukuRep {

    //private static Logger logger = LoggerFactory.getLogger(NaifukuRep.class);

    private class NaifukuDrug {
        int iyakuhincode;
        double amount;
        String tekiyouText;

        NaifukuDrug(int iyakuhincode, double amount, String tekiyouText){
            this.iyakuhincode = iyakuhincode;
            this.amount = amount;
            this.tekiyouText = tekiyouText;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            NaifukuDrug that = (NaifukuDrug) o;
            return iyakuhincode == that.iyakuhincode &&
                    Double.compare(that.amount, amount) == 0 &&
                    Objects.equals(tekiyouText, that.tekiyouText);
        }

        @Override
        public int hashCode() {

            return Objects.hash(iyakuhincode, amount, tekiyouText);
        }
    }

    private String usage;
    private Set<NaifukuDrug> drugs = new HashSet<>();
    private double kingaku;

    NaifukuRep(Naifuku drug) {
        this.usage = normalizeUsage(drug.usage);
        doAdd(drug);
    }

    boolean canAdd(Naifuku drug){
        return usage.equals(normalizeUsage(drug.usage));
    }

    void add(Naifuku drug){
        doAdd(drug);
    }

    int getTanka(){
        return RcptUtil.touyakuKingakuToTen(kingaku);
    }

    private void doAdd(Naifuku drug){
        NaifukuDrug naifukuDrug = new NaifukuDrug(drug.iyakuhincode, drug.amount, drug.tekiyou);
        drugs.add(naifukuDrug);
        kingaku += drug.yakka * drug.amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NaifukuRep that = (NaifukuRep) o;
        return Objects.equals(usage, that.usage) &&
                Objects.equals(drugs, that.drugs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usage, drugs);
    }

    private static boolean newUsageNormalization;
    static {
        newUsageNormalization = Boolean.getBoolean("jp.chang.myclinic.new_usage_normalization");
        System.err.println("newUsageNormalization: " + newUsageNormalization);
    }

    // TODO: more aggresively normalize usage (e.g., 分２ (2-1) 朝夕食後)
    private String normalizeUsage(String usage){
        if( newUsageNormalization ){
            for(String s: List.of("就寝前", "眠前")){
                if( usage.contains(s) ){
                    usage = usage.replace(s, "寝る前");
                }
            }
            System.err.println("usage: " + usage);
            return usage;
        } else {
            if (usage.equals("就寝前")) {
                return "寝る前";
            } else {
                return usage;
            }
        }
    }
}
