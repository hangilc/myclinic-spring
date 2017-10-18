package jp.chang.myclinic.util;

import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import jp.chang.myclinic.dto.PrescExampleDTO;
import jp.chang.myclinic.dto.PrescExampleFullDTO;

import java.text.NumberFormat;

public class PrescExampleUtil {
    private static NumberFormat numberFormat = NumberFormat.getNumberInstance();

    public static String rep(PrescExampleFullDTO prescExampleFull){
        PrescExampleDTO prescExample = prescExampleFull.prescExample;
        IyakuhinMasterDTO master = prescExampleFull.master;
        DrugCategory category = DrugCategory.fromCode(prescExample.category);
        if( category == null ){
            return "Unknown presc example category: " + prescExample.category;
        }
        Double amount = Double.valueOf(prescExample.amount);
        switch(category){
            case Naifuku: {
                return String.format("%s %s%s %s %d日分",
                        master.name,
                        numberFormat.format(amount),
                        master.unit,
                        prescExample.usage,
                        prescExample.days);
            }
            case Tonpuku: {
                return String.format("%s １回 %s%s %s %d回分",
                        master.name,
                        numberFormat.format(amount),
                        master.unit,
                        prescExample.usage,
                        prescExample.days);
            }
            case Gaiyou: {
                return String.format("%s %s%s %s",
                        master.name,
                        numberFormat.format(amount),
                        master.unit,
                        prescExample.usage);
            }
            default: return "Unknown presc example category: " + prescExample.category;
        }
    }

}
