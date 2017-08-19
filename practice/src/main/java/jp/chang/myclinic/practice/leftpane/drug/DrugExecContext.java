package jp.chang.myclinic.practice.leftpane.drug;

import java.util.List;

interface DrugExecContext {
    void onDrugDeleted(List<Integer> drugIds);
}
