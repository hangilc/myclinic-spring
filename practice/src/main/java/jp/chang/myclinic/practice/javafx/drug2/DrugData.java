package jp.chang.myclinic.practice.javafx.drug2;

import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.consts.Zaikei;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.util.DrugUtil;

public class DrugData {

    //private static Logger logger = LoggerFactory.getLogger(DrugData.class);

    // common part
    private int iyakuhincode;
    private String masterValidFrom;
    private String name;
    private String unit;
    private Zaikei zaikei;

    // presc example and drug part
    private Double amount;
    private String usage;
    private DrugCategory category;
    private Integer days;

    // presc example specific part
    private int prescExampleId;
    private String comment;

    // drug specific part
    private int drugId;
    private Integer prescribed;

    private DrugData() {

    }

    public boolean isPrescExample(){
        return prescExampleId != 0;
    }

    public boolean isDrug(){
        return drugId != 0;
    }

    public static DrugData fromMaster(IyakuhinMasterDTO master){
        DrugData data = new DrugData();
        data.setMaster(master);
        return data;
    }

    public static DrugData fromExample(PrescExampleFullDTO example){
        DrugData data = new DrugData();
        data.setMaster(example.master);
        PrescExampleDTO ex = example.prescExample;
        data.prescExampleId = ex.prescExampleId;
        data.amount = Double.parseDouble(ex.amount);
        data.usage = ex.usage;
        data.category = DrugCategory.fromCode(ex.category);
        if( data.category == null ){
            throw new RuntimeException("Invalid category: " + ex.category);
        }
        data.days = ex.days;
        data.comment = example.prescExample.comment;
        return data;
    }

    public static DrugData fromDrug(DrugFullDTO drugFull){
        DrugData data = new DrugData();
        data.setMaster(drugFull.master);
        DrugDTO drug = drugFull.drug;
        data.drugId = drug.drugId;
        data.amount = drug.amount;
        data.usage = drug.usage;
        data.category = DrugCategory.fromCode(drug.category);
        if( data.category == null ){
            throw new RuntimeException("Invalid category: " + drug.category);
        }
        data.days = drug.days;
        data.prescribed = drug.prescribed;
        return data;
    }

    public void setMaster(IyakuhinMasterDTO master){
        this.iyakuhincode = master.iyakuhincode;
        this.masterValidFrom = master.validFrom;
        this.name = master.name;
        this.unit = master.unit;
        this.zaikei = Zaikei.fromCode(master.zaikei);
        if( zaikei == null ){
            throw new RuntimeException("Invalid zaikei: " + master.zaikei);
        }
    }

    public String rep(){
        if( category == null ){
            return name;
        } else {
            return DrugUtil.drugRep(category, name, amount, unit, usage, days);
        }
    }

    public int getPrescExampleId() {
        return prescExampleId;
    }

    public int getDrugId() {
        return drugId;
    }

    public int getIyakuhincode() {
        return iyakuhincode;
    }

    public String getMasterValidFrom() {
        return masterValidFrom;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public Zaikei getZaikei() {
        return zaikei;
    }

    public Double getAmount() {
        return amount;
    }

    public String getUsage() {
        return usage;
    }

    public Integer getDays() {
        return days;
    }

    public DrugCategory getCategory() {
        return category;
    }

    public String getComment() {
        return comment;
    }

}
