package jp.chang.myclinic.practice.javafx.shinryou;

import javafx.application.Platform;
import jp.chang.myclinic.dto.ShinryouAttrDTO;
import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.javafx.FunJavaFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class CopySelectedForm extends HandleSelectedForm {

    private int targetVisitId;
    private BiConsumer<List<ShinryouFullDTO>, Map<Integer, ShinryouAttrDTO>>
            onEnteredHandler = (ss, a) -> {
    };

    public CopySelectedForm(int targetVisitId, List<ShinryouFullDTO> shinryouList) {
        super("診療行為のコピー", shinryouList);
        this.targetVisitId = targetVisitId;
    }

    public void setOnEnteredHandler(BiConsumer<List<ShinryouFullDTO>, Map<Integer, ShinryouAttrDTO>>
                                            onEnteredHandler) {
        this.onEnteredHandler = onEnteredHandler;
    }

    @Override
    protected void onEnter(HandleSelectedForm form, List<ShinryouFullDTO> selection) {
        List<ShinryouFullDTO> enteredList = new ArrayList<>();
        Map<Integer, ShinryouAttrDTO> attrMap = new HashMap<>();
        FunJavaFX.batchCopyShinryou(targetVisitId, selection,
                (entered, attr) -> {
                    enteredList.add(entered);
                    if( attr != null ){
                        attrMap.put(attr.shinryouId, attr);
                    }
                }, () -> Platform.runLater(() -> onEnteredHandler.accept(enteredList, attrMap)));
    }

}
