package jp.chang.myclinic.practice.javafx.drug.lib;

import jp.chang.myclinic.consts.DrugCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DrugEnterInputStateController {

    void updateInput(DrugEnterInput input, DrugEnterInputState state){
        state = state.copy();
        DrugEnterInputState prev = new DrugEnterInputState();
        input.getStateTo(prev);
        adapt(prev, state);
        input.setStateFrom(state);
    }

    void adapt(DrugEnterInputState prev, DrugEnterInputState state){
        DrugCategory category = state.getCategory();
        if( category == DrugCategory.Gaiyou ){

        }
        if( state.isDaysFixed() ){
            if( category == DrugCategory.Naifuku || category == DrugCategory.Tonpuku ){
                String prevDays = prev.getDays();
                if( prevDays != null && !prevDays.isEmpty() ){
                    state.setDays(prevDays);
                }
            }
        }
    }

}
