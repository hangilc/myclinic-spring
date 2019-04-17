package jp.chang.myclinic.practice.javafx.shinryou;

import javafx.application.Platform;
import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.dto.ShinryouFullWithAttrDTO;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.lib.CFUtil;
import jp.chang.myclinic.utilfx.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static java.util.stream.Collectors.toList;

public class DeleteSelectedForm extends HandleSelectedForm {

    private Consumer<List<Integer>> onDeletedHandler = ids -> {};

    DeleteSelectedForm(List<ShinryouFullWithAttrDTO> shinryouList) {
        super("複数診療削除", shinryouList);
    }

    public void setOnDeletedHandler(Consumer<List<Integer>> onDeletedHandler) {
        this.onDeletedHandler = onDeletedHandler;
    }

    @Override
    protected void onEnter(List<ShinryouFullWithAttrDTO> selection) {
        List<Integer> shinryouIds = selection.stream()
                .map(s -> s.shinryou.shinryou.shinryouId)
                .collect(toList());
        Context.frontend.batchDeleteShinryouCascading(shinryouIds)
                .thenAcceptAsync(v -> onDeletedHandler.accept(shinryouIds), Platform::runLater)
                .exceptionally(HandlerFX::exceptionally);
    }

}
