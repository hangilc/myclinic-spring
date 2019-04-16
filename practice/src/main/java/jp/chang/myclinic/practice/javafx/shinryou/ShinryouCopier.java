package jp.chang.myclinic.practice.javafx.shinryou;

import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.frontend.Frontend;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.utilfx.HandlerFX;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.toList;

class ShinryouCopier {

    private int srcVisitId;
    private int dstVisitId;
    private List<ShinryouWithAttrDTO> dstShinryouList;

    public ShinryouCopier(int srcVisitId, int dstVisitId) {
        this.srcVisitId = srcVisitId;
        this.dstVisitId = dstVisitId;
    }

    public CompletableFuture<List<DrugFullWithAttrDTO>> copy() {
        Frontend frontend = Context.frontend;
        frontend.listShinryouWithAttr(srcVisitId)
                .thenCompose(srcList -> {
                    BatchEnterRequestDTO request = new BatchEnterRequestDTO();
                    request.shinryouList = srcList.stream().map(this::copyShinryou).collect(toList());
                    return frontend.batchEnter(request);
                })
                .thenApply(result -> {
                    return frontend.listShinryouFullWithAttrByIds(result.shinryouIds);
                })
                .exceptionally(HandlerFX::exceptionally);
    }

    private ShinryouWithAttrDTO copyShinryou(ShinryouWithAttrDTO src) {
        ShinryouWithAttrDTO dst = new ShinryouWithAttrDTO();
        dst.shinryou = ShinryouDTO.copy(src.shinryou);
        dst.shinryou.shinryouId = 0;
        dst.shinryou.visitId = dstVisitId;
        if( src.attr != null ){
            dst.attr = ShinryouAttrDTO.copy(src.attr);
            dst.attr.shinryouId = 0;
        }
        return dst;
    }

    private void extendAttr(ShinryouAttrDTO attr) {

    }
}
