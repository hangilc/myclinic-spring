package jp.chang.myclinic.practice.javafx.drug2;

import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.consts.Zaikei;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import jp.chang.myclinic.dto.PrescExampleDTO;
import jp.chang.myclinic.dto.PrescExampleFullDTO;
import jp.chang.myclinic.util.DrugUtil;

public class DrugData {

    //private static Logger logger = LoggerFactory.getLogger(DrugData.class);
    private int iyakuhincode;
    private int prescExampleId;
    private int drugId;
    private String name;
    private String unit;
    private Zaikei zaikei;
    private Double amount;
    private String usage;
    private Integer days;
    private DrugCategory category;
    private String comment;

    private DrugData() {

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

    public String rep(){
        if( category == null ){
            return name;
        } else {
            return DrugUtil.drugRep(category, name, amount, unit, usage, days);
        }
    }

    public int getIyakuhincode() {
        return iyakuhincode;
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

    private void setMaster(IyakuhinMasterDTO master){
        this.iyakuhincode = master.iyakuhincode;
        this.name = master.name;
        this.unit = master.unit;
        this.zaikei = Zaikei.fromCode(master.zaikei);
        if( zaikei == null ){
            throw new RuntimeException("Invalid zaikei: " + master.zaikei);
        }
    }

}
