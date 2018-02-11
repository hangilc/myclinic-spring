package jp.chang.myclinic.practice.lib.drug;

import jp.chang.myclinic.consts.DrugCategory;

public interface DrugFormGetter {

    int getIyakuhincode();
    String getAmount();
    String getUsage();
    String getDays();
    DrugCategory getCategory();

}

