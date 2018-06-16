package jp.chang.myclinic.util;

import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.*;

import java.text.NumberFormat;

/**
 * Created by hangil on 2017/06/13.
 */
public class DrugUtil {

    private static NumberFormat numberFormat = NumberFormat.getNumberInstance();

    public static String drugRep(DrugDTO drug, IyakuhinMasterDTO master){
        DrugCategory category = DrugCategory.fromCode(drug.category);
        if( category == null ){
            return "Unknown drug category: " + drug.category;
        }
        switch(category){
            case Naifuku: {
                return String.format("%s %s%s %s %d日分", master.name,
                        numberFormat.format(drug.amount),
                        master.unit,
                        drug.usage,
                        drug.days);
            }
            case Tonpuku: {
                return String.format("%s １回 %s%s %s %d回分", master.name,
                        numberFormat.format(drug.amount),
                        master.unit,
                        drug.usage,
                        drug.days);
            }
            case Gaiyou: {
                return String.format("%s %s%s %s", master.name,
                        numberFormat.format(drug.amount),
                        master.unit,
                        drug.usage);
            }
            default: return "Unknown drug category: " + drug.category;
        }
    }

    public static String drugRep(DrugFullDTO drugFull){
        DrugDTO drug = drugFull.drug;
        IyakuhinMasterDTO master = drugFull.master;
        return drugRep(drug, master);
    }

    public static String conductDrugRep(ConductDrugFullDTO drugFull){
        return conductDrugRep(drugFull.conductDrug, drugFull.master);
    }

    public static String conductDrugRep(ConductDrugDTO dto, IyakuhinMasterDTO master){
        return String.format("%s %s%s", master.name, numberFormat.format(dto.amount), master.unit);
    }

}
