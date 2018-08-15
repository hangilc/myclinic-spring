package jp.chang.myclinic.practice.javafx;

import javafx.application.Platform;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.BatchEnterResultDTO;
import jp.chang.myclinic.dto.ConductFullDTO;
import jp.chang.myclinic.dto.ShinryouAttrDTO;
import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.practice.lib.ErrorMessageExtractor;
import jp.chang.myclinic.practice.lib.shinryou.ShinryouCopier;
import jp.chang.myclinic.utilfx.GuiUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class FunJavaFX {

    public static Consumer<Throwable> createErrorHandler() {
        return (Throwable th) -> {
            String message = ErrorMessageExtractor.extract(th);
            Platform.runLater(() -> GuiUtil.alertError(message));
        };
    }

    public static void batchCopyShinryou(int targetVisitId, List<ShinryouFullDTO> srcList,
                                         BiConsumer<ShinryouFullDTO, ShinryouAttrDTO> onEntered,
                                         Runnable onEnd) {
        ShinryouCopier copier = new ShinryouCopier(targetVisitId, srcList, onEntered, createErrorHandler(),
                () -> Platform.runLater(onEnd));
        copier.start();
    }

    public static class BatchEnteredShinryou {
        public List<ShinryouFullDTO> shinryouList;
        public List<ConductFullDTO> conducts;
        public Map<Integer, ShinryouAttrDTO> attrMap;
    }

    public static void batchEnterShinryouByNames(int visitId, List<String> names,
                                                 Consumer<BatchEnteredShinryou> cb) {
        class Local {
            private BatchEnterResultDTO enterResult;
            private List<ShinryouFullDTO> shinryouList;
            private List<ConductFullDTO> conducts;
        }
        Local local = new Local();
        Service.api.batchEnterShinryouByName(names, visitId)
                .thenCompose(result -> {
                    local.enterResult = result;
                    return Service.api.listShinryouFullByIds(local.enterResult.shinryouIds);
                })
                .thenCompose(result -> {
                    local.shinryouList = result;
                    return Service.api.listConductFullByIds(local.enterResult.conductIds);
                })
                .thenCompose(result -> {
                    local.conducts = result;
                    List<Integer> shinryouIds = local.enterResult.shinryouIds;
                    return Service.api.batchGetShinryouAttr(shinryouIds);
                })
                .thenAccept(attrList -> {
                    Map<Integer, ShinryouAttrDTO> attrMap = new HashMap<>();
                    attrList.forEach(attr -> attrMap.put(attr.shinryouId, attr));
                    BatchEnteredShinryou arg = new BatchEnteredShinryou();
                    arg.shinryouList = local.shinryouList;
                    arg.conducts = local.conducts;
                    arg.attrMap = attrMap;
                    Platform.runLater(() -> cb.accept(arg));
                })
                .exceptionally(ex -> {
                    FunJavaFX.createErrorHandler().accept(ex);
                    return null;
                });
    }

}
