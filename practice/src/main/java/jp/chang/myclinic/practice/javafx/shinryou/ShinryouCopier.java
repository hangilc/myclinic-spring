package jp.chang.myclinic.practice.javafx.shinryou;

import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.frontend.Frontend;
import jp.chang.myclinic.practice.Context;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.toList;

class ShinryouCopier {

    static class Result {
        List<ShinryouFullDTO> shinryouList;
        Map<Integer, ShinryouAttrDTO> attrMap;
    }

    private int srcVisitId;
    private int dstVisitId;
    private List<ShinryouWithAttrDTO> dstShinryouList;

    public ShinryouCopier(int srcVisitId, int dstVisitId) {
        this.srcVisitId = srcVisitId;
        this.dstVisitId = dstVisitId;
    }

    public CompletableFuture<Result> copy() {
        return null;
//        Frontend frontend = Context.frontend;
//        BatchEnterRequestDTO request = new BatchEnterRequestDTO();
//        return frontend.listShinryou(srcVisitId)
//                .thenCompose(srcShinryouList -> {
//                    this.dstShinryouList = srcShinryouList.stream()
//                            .map(this::copyShinryou).collect(toList());
//                    List<Integer> shinryouIds = srcShinryouList.stream()
//                            .map(s -> s.shinryouId).collect(toList());
//                    return frontend.batchGetShinryouAttr(shinryouIds);
//                });
    }

    private ShinryouWithAttrDTO copyShinryou(ShinryouDTO src) {
        ShinryouWithAttrDTO dst = new ShinryouWithAttrDTO();
        dst.shinryou = ShinryouDTO.copy(src);
        dst.shinryou.shinryouId = 0;
        dst.shinryou.visitId = dstVisitId;
        return dst;
    }

    private void extendAttr(ShinryouAttrDTO attr) {

    }
}
