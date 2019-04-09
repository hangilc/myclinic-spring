package jp.chang.myclinic.practice.javafx.drug.lib2;

import jp.chang.myclinic.consts.DrugCategory;

import static jp.chang.myclinic.consts.DrugCategory.Gaiyou;
import static jp.chang.myclinic.consts.DrugCategory.Naifuku;

class FixedDaysService {

    private String daysBackup = "";

    String getAdjustedDaysInput(String requestDays, boolean daysFixed, DrugCategory category) {
        if( category == Naifuku && daysFixed && !daysBackup.isEmpty()) {
            return daysBackup;
        } else {
            return requestDays;
        }
    }

    void onDaysSubmitted(String days, DrugCategory category) {
        if (category == Naifuku) {
            this.daysBackup = days;
        }
    }

}