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

    public static void stuffMasterInto(IyakuhinMasterDTO master, DrugInputModel model){
        model.setIyakuhincode(master.iyakuhincode);
        model.setDrugName(master.name);
        model.setAmount("");
        model.setAmountUnit(master.unit);
        model.setUsage("");
        model.setDays("");
        model.setCategory(PracticeUtil.zaikeiToCategory(master.zaikei));
        model.setComment("");
   }

}
