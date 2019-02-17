package jp.chang.myclinic.practice.javafx.drug.lib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DrugInputStateController {

    void adapt(DrugInputState state){
        String comment = state.getComment();
        state.setCommentVisible(!(comment == null || comment.isEmpty()));
        String tekiyou = state.getTekiyou();
        state.setTekiyouVisible(!(tekiyou == null || tekiyou.isEmpty()));
    }
}
