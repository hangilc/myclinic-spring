package jp.chang.myclinic.practice.lib;

import jp.chang.myclinic.dto.ShinryouDTO;
import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

public class PracticeAPI {

    private static Logger logger = LoggerFactory.getLogger(PracticeAPI.class);

    private static class IgnoreException extends RuntimeException {

    }

    public static CompletableFuture<ShinryouFullDTO> copyShinryou(VisitDTO target, ShinryouFullDTO src){
        return Service.api.resolveShinryoucode(src.shinryou.shinryoucode, target.visitedAt)
                .thenCompose(shinryoucode -> {
                    if( shinryoucode == 0 ){
                        throw new IgnoreException();
                    } else {
                        ShinryouDTO shinryou = ShinryouDTO.copy(src.shinryou);
                        shinryou.shinryouId = 0;
                        shinryou.visitId = target.visitId;
                        shinryou.shinryoucode = shinryoucode;
                        return Service.api.enterShinryou(shinryou);
                    }
                })
                .thenCompose(Service.api::getShinryouFull)
                .exceptionally(ex -> {
                    if( ex instanceof CompletionException ){
                        CompletionException ce = (CompletionException)ex;
                        if( ce.getCause() instanceof IgnoreException ){
                            return null;
                        } else {
                            throw ce;
                        }
                    } else {
                        throw new CompletionException(ex);
                    }
                });
    }

    public static CompletableFuture<List<ShinryouFullDTO>> batchCopyShinryou(int visitId, List<ShinryouFullDTO> srcList){
        return Service.api.getVisit(visitId)
                .thenCompose(visit -> CFUtil.map(srcList, s -> copyShinryou(visit, s)))
                .thenApply(entered -> entered.stream().filter(Objects::nonNull).collect(Collectors.toList()));
    }

}
