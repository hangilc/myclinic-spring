package jp.chang.myclinic.backenddb;

import jp.chang.myclinic.consts.PharmaQueueState;
import jp.chang.myclinic.consts.WqueueWaitState;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.util.DateTimeUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class DbBackendService {

    private DbBackend dbBackend;

    public DbBackendService(DbBackend dbBackend) {
        this.dbBackend = dbBackend;
    }

    private void setWqueueState(int visitId, WqueueWaitState state){
        WqueueDTO wqueue = dbBackend.query(b -> b.getWqueue(visitId));
        if( wqueue == null ){
            WqueueDTO newWqueue = new WqueueDTO();
            newWqueue.visitId = visitId;
            newWqueue.waitState = state.getCode();
            dbBackend.txProc(b -> b.enterWqueue(newWqueue));
        } else {
            WqueueDTO updatedWqueue = WqueueDTO.copy(wqueue);
            updatedWqueue.waitState = state.getCode();
            dbBackend.txProc(b -> b.updateWqueue(updatedWqueue));
        }
    }

    public VisitDTO startVisit(int patientId, LocalDateTime at) {
        LocalDate atDate = at.toLocalDate();
        VisitDTO visitDTO = new VisitDTO();
        visitDTO.patientId = patientId;
        visitDTO.visitedAt = DateTimeUtil.toSqlDateTime(at);
        {
            List<ShahokokuhoDTO> list = dbBackend.query(b -> b.findAvailableShahokokuho(patientId, atDate));
            if (list.size() == 0) {
                visitDTO.shahokokuhoId = 0;
            } else {
                visitDTO.shahokokuhoId = list.get(0).shahokokuhoId;
            }
        }
        {
            List<KoukikoureiDTO> list = dbBackend.query(b -> b.findAvailableKoukikourei(patientId, atDate));
            if (list.size() == 0) {
                visitDTO.koukikoureiId = 0;
            } else {
                visitDTO.koukikoureiId = list.get(0).koukikoureiId;
            }
        }
        {
            List<RoujinDTO> list = dbBackend.query(b -> b.findAvailableRoujin(patientId, atDate));
            if (list.size() == 0) {
                visitDTO.roujinId = 0;
            } else {
                visitDTO.roujinId = list.get(0).roujinId;
            }
        }
        {
            visitDTO.kouhi1Id = 0;
            visitDTO.kouhi2Id = 0;
            visitDTO.kouhi3Id = 0;
            List<KouhiDTO> list = dbBackend.query(b -> b.findAvailableKouhi(patientId, atDate));
            int n = list.size();
            if (n > 0) {
                visitDTO.kouhi1Id = list.get(0).kouhiId;
                if (n > 1) {
                    visitDTO.kouhi2Id = list.get(1).kouhiId;
                    if (n > 2) {
                        visitDTO.kouhi3Id = list.get(2).kouhiId;
                    }
                }
            }
        }
        dbBackend.txProc(b -> b.enterVisit(visitDTO));
        WqueueDTO wqueueDTO = new WqueueDTO();
        wqueueDTO.visitId = visitDTO.visitId;
        wqueueDTO.waitState = WqueueWaitState.WaitExam.getCode();
        dbBackend.txProc(b -> b.enterWqueue(wqueueDTO));
        return visitDTO;
    }

    public void startExam(int visitId){
        WqueueDTO wqueue = dbBackend.query(b -> b.getWqueue(visitId));
        if( wqueue == null ){
            throw new RuntimeException("Cannot start exam because wqueue is null");
        }
        WqueueDTO updated = WqueueDTO.copy(wqueue);
        updated.waitState = WqueueWaitState.InExam.getCode();
        dbBackend.txProc(b -> b.updateWqueue(updated));
    }

    public void suspendExam(int visitId){
        setWqueueState(visitId, WqueueWaitState.WaitReExam);
    }

    public void endExam(int visitId, int charge) {
        VisitDTO visit = dbBackend.query(b -> b.getVisit(visitId));
        if (visit == null) {
            throw new RuntimeException("No such visit: " + visitId);
        }
        boolean isToday = DateTimeUtil.parseSqlDateTime(visit.visitedAt).toLocalDate().equals(LocalDate.now());
        ChargeDTO prevCharge = dbBackend.query(b -> b.getCharge(visitId));
        if (prevCharge != null) {
            ChargeDTO newCharge = ChargeDTO.copy(prevCharge);
            newCharge.charge = charge;
            dbBackend.txProc(b -> b.updateCharge(newCharge));
        } else {
            ChargeDTO newCharge = new ChargeDTO();
            newCharge.visitId = visitId;
            newCharge.charge = charge;
            dbBackend.txProc(b -> b.enterCharge(newCharge));
        }
        WqueueDTO wqueue = dbBackend.query(b -> b.getWqueue(visitId));
        if (wqueue != null) {
            WqueueDTO newWqueue = WqueueDTO.copy(wqueue);
            newWqueue.waitState = WqueueWaitState.WaitCashier.getCode();
            dbBackend.txProc(b -> b.updateWqueue(newWqueue));
        } else {
            WqueueDTO newWqueue = new WqueueDTO();
            newWqueue.visitId = visitId;
            newWqueue.waitState = WqueueWaitState.WaitCashier.getCode();
            dbBackend.txProc(b -> b.enterWqueue(newWqueue));
        }
        PharmaQueueDTO pharmaQueue = dbBackend.query(b -> b.getPharmaQueue(visitId));
        if (pharmaQueue != null) {
            dbBackend.txProc(b -> b.deletePharmaQueue(visitId));
        }
        if (isToday) {
            int unprescribed = dbBackend.query(b -> b.countUnprescribedDrug(visitId));
            if (unprescribed > 0) {
                PharmaQueueDTO newPharmaQueue = new PharmaQueueDTO();
                newPharmaQueue.visitId = visitId;
                newPharmaQueue.pharmaState = PharmaQueueState.WaitPack.getCode();
                dbBackend.txProc(b -> b.enterPharmaQueue(newPharmaQueue));
            }
        }
    }

}
