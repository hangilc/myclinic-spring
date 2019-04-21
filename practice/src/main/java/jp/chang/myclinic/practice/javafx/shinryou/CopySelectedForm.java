package jp.chang.myclinic.practice.javafx.shinryou;

import javafx.application.Platform;
import jp.chang.myclinic.dto.ShinryouAttrDTO;
import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.dto.ShinryouFullWithAttrDTO;
import jp.chang.myclinic.dto.ShinryouWithAttrDTO;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.javafx.FunJavaFX;
import jp.chang.myclinic.utilfx.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static java.util.stream.Collectors.toList;

public class CopySelectedForm extends HandleSelectedForm {

    private int targetVisitId;
    private Consumer<List<ShinryouFullWithAttrDTO>> onEnteredHandler = ss -> {
    };

    public CopySelectedForm(int targetVisitId, List<ShinryouFullWithAttrDTO> shinryouList) {
        super("診療行為のコピー", shinryouList);
        this.targetVisitId = targetVisitId;
    }

    public void setOnEnteredHandler(Consumer<List<ShinryouFullWithAttrDTO>> onEnteredHandler) {
        this.onEnteredHandler = onEnteredHandler;
    }

    @Override
    protected void onEnter(List<ShinryouFullWithAttrDTO> selectionFull) {
        List<ShinryouWithAttrDTO> selection = selectionFull.stream()
                .map(full -> ShinryouWithAttrDTO.create(full.shinryou.shinryou, full.attr))
                .collect(toList());
        ShinryouCopier copier = new ShinryouCopier(targetVisitId);
        copier.copy(selection)
                .thenAcceptAsync(result -> onEnteredHandler.accept(result), Platform::runLater)
                .exceptionally(HandlerFX.exceptionally(this));
    }

}
