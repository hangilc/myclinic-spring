package jp.chang.myclinic.util;

import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.NumberFormat;

public class DrugUtil {
    private static Logger logger = LoggerFactory.getLogger(DrugUtil.class);

    private static NumberFormat numberFormat = NumberFormat.getNumberInstance();

    public static String drugRep(DrugCategory category, String name, double amount, String unit,
                                 String usage, int days){
        switch (category) {
            case Naifuku: {
                return String.format("%s %s%s %s %d日分", name,
                        numberFormat.format(amount),
                        unit,
                        usage,
                        days);
            }
            case Tonpuku: {
                return String.format("%s １回 %s%s %s %d回分", name,
                        numberFormat.format(amount),
                        unit,
                        usage,
                        days);
            }
            case Gaiyou: {
                return String.format("%s %s%s %s", name,
                        numberFormat.format(amount),
                        unit,
                        usage);
            }
            default:
                logger.warn("Unknown drug category: " + category);
                return String.format("%s %s %s %s", name, numberFormat.format(amount), unit, usage);
        }
    }

    public static String drugRep(DrugDTO drug, IyakuhinMasterDTO master, DrugAttrDTO attr) {
        if (attr != null && attr.tekiyou != null && !attr.tekiyou.isEmpty()) {
            return drugRep(drug, master) + String.format(" [摘要：%s]", attr.tekiyou);
        } else {
            return drugRep(drug, master);
        }
    }

    public static String drugRep(DrugDTO drug, IyakuhinMasterDTO master) {
        DrugCategory category = DrugCategory.fromCode(drug.category);
        if (category == null) {
            return "Unknown drug category: " + drug.category;
        }
        return drugRep(category, master.name, drug.amount, master.unit, drug.usage, drug.days);
//        switch (category) {
//            case Naifuku: {
//                return String.format("%s %s%s %s %d日分", master.name,
//                        numberFormat.format(drug.amount),
//                        master.unit,
//                        drug.usage,
//                        drug.days);
//            }
//            case Tonpuku: {
//                return String.format("%s １回 %s%s %s %d回分", master.name,
//                        numberFormat.format(drug.amount),
//                        master.unit,
//                        drug.usage,
//                        drug.days);
//            }
//            case Gaiyou: {
//                return String.format("%s %s%s %s", master.name,
//                        numberFormat.format(drug.amount),
//                        master.unit,
//                        drug.usage);
//            }
//            default:
//                return "Unknown drug category: " + drug.category;
//        }
    }

    public static String drugRep(DrugFullDTO drugFull) {
        DrugDTO drug = drugFull.drug;
        IyakuhinMasterDTO master = drugFull.master;
        return drugRep(drug, master);
    }

    public static String drugRep(DrugFullDTO drugFull, DrugAttrDTO attr) {
        DrugDTO drug = drugFull.drug;
        IyakuhinMasterDTO master = drugFull.master;
        return drugRep(drug, master, attr);
    }

    public static String conductDrugRep(ConductDrugFullDTO drugFull) {
        return conductDrugRep(drugFull.conductDrug, drugFull.master);
    }

    public static String conductDrugRep(ConductDrugDTO dto, IyakuhinMasterDTO master) {
        return String.format("%s %s%s", master.name, numberFormat.format(dto.amount), master.unit);
    }

}
