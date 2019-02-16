package jp.chang.myclinic.practice.javafx.drug.lib;

import jp.chang.myclinic.consts.DrugCategory;

public class DrugInputBaseStateController {

    public void adaptToCategory(DrugInputBaseState state){
        DrugCategory category = state.getCategory();
        if (category != null) {
            switch (category) {
                case Naifuku: {
                    state.setAmountLabel("用量：");
                    state.setDaysLabel("日数：");
                    state.setDaysUnit("日分");
                    state.setDaysVisible(true);
                    break;
                }
                case Tonpuku: {
                    state.setAmountLabel("一回：");
                    state.setDaysLabel("回数：");
                    state.setDaysUnit("回分");
                    state.setDaysVisible(true);
                    break;
                }
                case Gaiyou: {
                    state.setAmountLabel("用量：");
                    state.setDaysVisible(false);
                    break;
                }
            }
        }
    }

}
