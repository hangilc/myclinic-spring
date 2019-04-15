package jp.chang.myclinic.practice.javafx.shinryou;

import javafx.application.Platform;
import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.lib.CFUtil;
import jp.chang.myclinic.utilfx.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DeleteSelectedForm extends HandleSelectedForm {

    DeleteSelectedForm(List<ShinryouFullDTO> shinryouList) {
        super("複数診療削除", shinryouList);
    }

    @Override
    protected void onEnter(HandleSelectedForm form, List<ShinryouFullDTO> selection) {
        List<Integer> deletedShinryouIds = new ArrayList<>();
        CFUtil.forEach(selection, this::deleteShinryou)
                .thenAccept(result -> Platform.runLater(() -> hideWorkarea()))
                .exceptionally(HandlerFX::exceptionally);
    }

    private CompletableFuture<Void> deleteShinryou(ShinryouFullDTO shinryou) {
        return Context.frontend.deleteShinryou(shinryou.shinryou.shinryouId)
                .thenAccept(result -> Platform.runLater(() ->
                        fireShinryouDeletedEvent(shinryou.shinryou)));
    }

}
