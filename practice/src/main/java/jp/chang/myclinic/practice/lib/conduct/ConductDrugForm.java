package jp.chang.myclinic.practice.lib.conduct;

import jp.chang.myclinic.dto.ConductDrugDTO;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import jp.chang.myclinic.practice.lib.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

public interface ConductDrugForm {

    void setIyakuhincode(int iyakuhincode);
    void setName(String name);
    void setAmount(String amount);
    default void setAmount(double value){
        setAmount(ValueFormatter.formatDouble(value));
    }
    void setAmountUnit(String unit);

    int getIyakuhincode();
    String getAmount();

    default void setMaster(IyakuhinMasterDTO master){
        setIyakuhincode(master.iyakuhincode);
        setName(master.name);
        setAmountUnit(master.unit);
    }

    default List<String> stuffInto(ConductDrugDTO drug){
        List<String> errors = new ArrayList<>();
        {
            int iyakuhincode = getIyakuhincode();
            if( iyakuhincode != 0 ){
                drug.iyakuhincode = iyakuhincode;
            } else {
                errors.add("医薬品が設定されていません。");
            }
        }
        try {
            drug.amount = Double.parseDouble(getAmount());
        } catch(NumberFormatException ex){
            errors.add("用量の入力が不適切です。");
        }
        return errors;
    }

}
