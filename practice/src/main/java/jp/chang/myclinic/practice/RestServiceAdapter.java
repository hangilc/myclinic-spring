package jp.chang.myclinic.practice;

import jp.chang.myclinic.dto.DrugAttrDTO;
import jp.chang.myclinic.dto.ShinryouAttrDTO;
import jp.chang.myclinic.dto.ShoukiDTO;
import jp.chang.myclinic.dto.TextDTO;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class RestServiceAdapter implements RestService {

    @Override
    public CompletableFuture<Integer> enterText(TextDTO text) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> updateText(TextDTO textDTO) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> deleteText(int textId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<ShinryouAttrDTO>> batchGetShinryouAttr(List<Integer> shinryouIds) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<DrugAttrDTO>> batchGetDrugAttr(List<Integer> drugIds) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletionStage<List<ShoukiDTO>> batchGetShouki(List<Integer> visitIds) {
        throw new RuntimeException("not implemented");
    }

}
