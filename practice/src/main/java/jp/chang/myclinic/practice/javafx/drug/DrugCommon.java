package jp.chang.myclinic.practice.javafx.drug;

import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import jp.chang.myclinic.practice.lib.PracticeUtil;

public class DrugCommon {

    public static void bindDrugInputAndModel(DrugInput drugInput, DrugInputModel model){
        drugInput.drugNameProperty().bindBidirectional(model.drugNameProperty());
        drugInput.amountProperty().bindBidirectional(model.amountProperty());
        drugInput.amountUnitProperty().bindBidirectional(model.amountUnitProperty());
        drugInput.usageProperty().bindBidirectional(model.usageProperty());
        drugInput.daysProperty().bindBidirectional(model.daysProperty());
        drugInput.categoryProperty().bindBidirectional(model.categoryProperty());
        drugInput.commentProperty().bindBidirectional(model.commentProperty());
    }

    public static void stuffMasterInto(IyakuhinMasterDTO master, DrugInputModel model, InputConstraints constraints){
        model.setIyakuhincode(master.iyakuhincode);
        model.setDrugName(master.name);
        if( !constraints.isAmountFixed() ) {
            model.setAmount("");
        }
        model.setAmountUnit(master.unit);
        if( !constraints.isUsageFixed() ) {
            model.setUsage("");
        }
        if( !constraints.isDaysFixed() ) {
            model.setDays("");
        }
        model.setCategory(PracticeUtil.zaikeiToCategory(master.zaikei));
        model.setComment("");
   }

}
