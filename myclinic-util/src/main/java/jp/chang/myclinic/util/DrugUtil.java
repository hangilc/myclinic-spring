package jp.chang.myclinic.util;

import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;

import java.text.NumberFormat;

/**
 * Created by hangil on 2017/06/13.
 */
public class DrugUtil {

    private static NumberFormat numberFormat = NumberFormat.getNumberInstance();

    public static String drugRep(DrugFullDTO drugFull){
        DrugDTO drug = drugFull.drug;
        IyakuhinMasterDTO master = drugFull.master;
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
                return "";
            }
            case Gaiyou: {
                return "";
            }
            default: return "Unknown drug category: " + drug.category;
        }
    }
}
