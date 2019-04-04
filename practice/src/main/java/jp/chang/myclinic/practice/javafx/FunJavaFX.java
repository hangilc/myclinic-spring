package jp.chang.myclinic.practice.javafx;

import javafx.application.Platform;
import jp.chang.myclinic.consts.ConductKind;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.lib.ErrorMessageExtractor;
import jp.chang.myclinic.practice.lib.shinryou.ShinryouCopier;
import jp.chang.myclinic.utilfx.GuiUtil;

import java.util.ArrayList;
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
        BatchEnterByNamesRequestDTO req = new BatchEnterByNamesRequestDTO();
        req.shinryouNames = new ArrayList<>();
        req.conducts = new ArrayList<>();
        names.forEach(name -> {
            if( "骨塩定量".equals(name) ){
                addKotsuenTeiryou(req);
            } else {
                req.shinryouNames.add(name);
            }
        });
        Context.frontend.batchEnterByNames(visitId, req)
                .thenCompose(result -> {
                    local.enterResult = result;
                    return Context.frontend.listShinryouFullByIds(local.enterResult.shinryouIds);
                })
                .thenCompose(result -> {
                    local.shinryouList = result;
                    return Context.frontend.listConductFullByIds(local.enterResult.conductIds);
                })
                .thenCompose(result -> {
                    local.conducts = result;
                    List<Integer> shinryouIds = local.enterResult.shinryouIds;
                    return Context.frontend.batchGetShinryouAttr(shinryouIds);
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

    private static void addKotsuenTeiryou(BatchEnterByNamesRequestDTO req){
        EnterConductByNamesRequestDTO conductReq = new EnterConductByNamesRequestDTO();
        conductReq.kind = ConductKind.Gazou.getCode();
        conductReq.gazouLabel = "骨塩定量に使用";
        conductReq.shinryouNames = List.of("骨塩定量ＭＤ法");
        EnterConductKizaiByNamesRequestDTO kizai = new EnterConductKizaiByNamesRequestDTO();
        kizai.name = "四ツ切";
        kizai.amount = 1.0;
        conductReq.kizaiList = List.of(kizai);
        req.conducts.add(conductReq);
    }

}
