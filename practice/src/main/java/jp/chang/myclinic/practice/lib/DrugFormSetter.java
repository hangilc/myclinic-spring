package jp.chang.myclinic.practice.lib;

import jp.chang.myclinic.consts.DrugCategory;

import java.text.DecimalFormat;

public interface DrugFormSetter {

    void setDrugId(int drugId);
    void setVisitId(int visitId);
    void setIyakuhincode(int iyakuhincode);
    void setAmount(String value);
    default void setAmount(double value){
        DecimalFormat doubleFormatter = new DecimalFormat("###.##");
        setAmount(doubleFormatter.format(value));
    }
    void setUsage(String value);
    void setDays(String value);
    default void setDays(int value){
        setDays("" + value);
    }
    void setCategory(DrugCategory category);
    default void setCategory(int code){
        setCategory(DrugCategory.fromCode(code));
    }
    void setComment(String comment);

}
