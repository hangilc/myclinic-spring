package jp.chang.myclinic.practice.lib;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.consts.ConductKind;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.practice.Context;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PracticeAPI {

    //private static Logger logger = LoggerFactory.getLogger(PracticeAPI.class);

//    private static class IgnoreException extends RuntimeException {
//
//    }

//    public static CompletableFuture<ShinryouFullDTO> copyShinryou(VisitDTO target, ShinryouFullDTO src){
//        return Context.getInstance().getFrontend().resolveShinryoucode(src.shinryou.shinryoucode, target.visitedAt)
//                .thenCompose(shinryoucode -> {
//                    if( shinryoucode == 0 ){
//                        throw new IgnoreException();
//                    } else {
//                        ShinryouDTO shinryou = ShinryouDTO.copy(src.shinryou);
//                        shinryou.shinryouId = 0;
//                        shinryou.visitId = target.visitId;
//                        shinryou.shinryoucode = shinryoucode;
//                        return Context.getInstance().getFrontend().enterShinryou(shinryou);
//                    }
//                })
//                .thenCompose(Context.getInstance().getFrontend()::getShinryouFull)
//                .exceptionally(ex -> {
//                    if( ex instanceof CompletionException ){
//                        CompletionException ce = (CompletionException)ex;
//                        if( ce.getCause() instanceof IgnoreException ){
//                            return null;
//                        } else {
//                            throw ce;
//                        }
//                    } else {
//                        throw new CompletionException(ex);
//                    }
//                });
//    }

//    public static CompletableFuture<List<ShinryouFullDTO>> batchCopyShinryou(int visitId, List<ShinryouFullDTO> srcList){
//        return Context.getInstance().getFrontend().getVisit(visitId)
//                .thenCompose(visit -> CFUtil.map(srcList, s -> copyShinryou(visit, s)))
//                .thenApply(entered -> entered.stream().filter(Objects::nonNull).collect(Collectors.toList()));
//    }

    public static CompletableFuture<ConductFullDTO> enterInjection(int visitId, ConductKind kind, ConductDrugDTO drug){
        List<ConductShinryouDTO> shinryouList = new ArrayList<>();
        return Context.getInstance().getFrontend().getVisit(visitId)
                .thenCompose(result -> {
                    LocalDate at = LocalDateTime.parse(result.visitedAt).toLocalDate();
                    if( kind == ConductKind.HikaChuusha ){
                        return Context.getInstance().getFrontend().resolveShinryouMasterByName(List.of("皮下筋注"), at);
                    } else if( kind == ConductKind.JoumyakuChuusha ){
                        return Context.getInstance().getFrontend().resolveShinryouMasterByName(List.of("静注"), at);
                    } else {
                        return CompletableFuture.completedFuture(null);
                    }
                })
                .thenCompose(master -> {
                    if( master != null ){
                        ConductShinryouDTO shinryou = new ConductShinryouDTO();
                        shinryou.shinryoucode = master.shinryoucode;
                        shinryouList.add(shinryou);
                    }
                    ConductEnterRequestDTO req = new ConductEnterRequestDTO();
                    req.visitId = visitId;
                    req.kind = kind.getCode();
                    req.shinryouList = shinryouList;
                    req.drugs = Collections.singletonList(drug);
                    return Context.getInstance().getFrontend().enterConductFull(req);
                });
    }

    public static CompletableFuture<ShuushokugoMasterDTO> findDiseaseSusp(){
        return Context.getInstance().getFrontend().getShuushokugoMasterByName("の疑い");

    }

}
