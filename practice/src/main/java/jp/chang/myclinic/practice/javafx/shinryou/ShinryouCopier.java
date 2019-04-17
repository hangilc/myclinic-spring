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

    private int dstVisitId;

    ShinryouCopier(int dstVisitId) {
        this.dstVisitId = dstVisitId;
    }

    CompletableFuture<List<ShinryouFullWithAttrDTO>> copyFromVisit(int srcVisitId) {
        Frontend frontend = Context.frontend;
        return frontend.listShinryouWithAttr(srcVisitId)
                .thenCompose(this::copy);
    }

    CompletableFuture<List<ShinryouFullWithAttrDTO>> copy(List<ShinryouWithAttrDTO> srcList){
        Frontend frontend = Context.frontend;
        return frontend.batchEnter(convertToRequest(srcList))
                .thenCompose(result -> frontend.listShinryouFullWithAttrByIds(result.shinryouIds));
    }

    private BatchEnterRequestDTO convertToRequest(List<ShinryouWithAttrDTO> srcList){
        BatchEnterRequestDTO request = new BatchEnterRequestDTO();
        request.shinryouList = srcList.stream().map(this::copyShinryou).collect(toList());
        return request;
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

}
