package jp.chang.myclinic.practice.javafx.shohousen;

import jp.chang.myclinic.dto.HokenDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class ShohousenInfo {

    private static Logger logger = LoggerFactory.getLogger(ShohousenInfo.class);
    private VisitDTO visit;
    private HokenDTO hoken;

    private ShohousenInfo() {

    }

    public static CompletableFuture<ShohousenInfo> load(int visitId){
        ShohousenInfo info = new ShohousenInfo();
        return Context.frontend.getVisit(visitId)
                .thenCompose(visit -> {
                    info.visit = visit;
                    return Context.frontend.getHoken(visitId);
                })
                .thenApply(hoken -> {
                    info.hoken = hoken;
                    return info;
                });
    }

    public VisitDTO getVisit() {
        return visit;
    }

    public HokenDTO getHoken() {
        return hoken;
    }
}
