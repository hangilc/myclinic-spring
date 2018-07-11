package jp.chang.myclinic.practice.javafx;

import javafx.application.Platform;
import jp.chang.myclinic.dto.BatchEnterResultDTO;
import jp.chang.myclinic.dto.ConductFullDTO;
import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.practice.lib.ErrorMessageExtractor;
import jp.chang.myclinic.practice.lib.shinryou.ShinryouCopier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class FunJavaFX {

    public static Consumer<Throwable> createErrorHandler(){
        return (Throwable th) -> {
            String message = ErrorMessageExtractor.extract(th);
            Platform.runLater(() -> GuiUtil.alertError(message));
        };
    }

    public static void batchCopyShinryou(int targetVisitId, List<ShinryouFullDTO> srcList, Consumer<ShinryouFullDTO> onEntered,
                                  Runnable onEnd){
        ShinryouCopier copier = new ShinryouCopier(targetVisitId, srcList, onEntered, createErrorHandler(), onEnd);
        copier.start();
    }

    public static void batchEnterShinryouByNames(int visitId, List<String> names,
                                                 BiConsumer<List<ShinryouFullDTO>, List<ConductFullDTO>> cb){
        BatchEnterResultDTO enterResult = new BatchEnterResultDTO();
        List<ShinryouFullDTO> shinryouList = new ArrayList<>();
        Service.api.batchEnterShinryouByName(names, visitId)
                .thenCompose(result -> {
                    BatchEnterResultDTO.assign(enterResult, result);
                    return Service.api.listShinryouFullByIds(enterResult.shinryouIds);
                })
                .thenCompose(result -> {
                    shinryouList.addAll(result);
                    return Service.api.listConductFullByIds(enterResult.conductIds);
                })
                .thenAccept(conductList -> {
                    cb.accept(shinryouList, conductList);
                })
                .exceptionally(ex -> {
                    FunJavaFX.createErrorHandler().accept(ex);
                    return null;
                });
    }

}
