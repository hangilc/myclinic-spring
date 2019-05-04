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

    private void setWqueueState(int visitId, WqueueWaitState state) {
        WqueueDTO wqueue = dbBackend.query(b -> b.getWqueue(visitId));
        if (wqueue == null) {
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

    public void backendEnterVisit(VisitDTO visit) {
        dbBackend.txProc(backend -> backend.enterVisit(visit));
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

    public void startExam(int visitId) {
        WqueueDTO wqueue = dbBackend.query(b -> b.getWqueue(visitId));
        if (wqueue == null) {
            throw new RuntimeException("Cannot start exam because wqueue is null");
        }
        WqueueDTO updated = WqueueDTO.copy(wqueue);
        updated.waitState = WqueueWaitState.InExam.getCode();
        dbBackend.txProc(b -> b.updateWqueue(updated));
    }

    public void suspendExam(int visitId) {
        setWqueueState(visitId, WqueueWaitState.WaitReExam);
    }

    public void endExam(int visitId, int charge) {
        VisitDTO visit = dbBackend.query(b -> b.getVisit(visitId));
        if (visit == null) {
            throw new RuntimeException("No such visit: " + visitId);
        }
        ChargeDTO newCharge = new ChargeDTO();
        newCharge.visitId = visitId;
        newCharge.charge = charge;
        dbBackend.txProc(b -> b.enterCharge(newCharge));
        WqueueDTO wqueue = dbBackend.query(b -> b.getWqueue(visitId));
        if (wqueue == null) {
            throw new RuntimeException("Cannot find wqueue in endExam: " + visit);
        }
        WqueueDTO newWqueue = WqueueDTO.copy(wqueue);
        newWqueue.waitState = WqueueWaitState.WaitCashier.getCode();
        dbBackend.txProc(b -> b.updateWqueue(newWqueue));
        int unprescribed = dbBackend.query(b -> b.countUnprescribedDrug(visitId));
        if (unprescribed > 0) {
            PharmaQueueDTO newPharmaQueue = new PharmaQueueDTO();
            newPharmaQueue.visitId = visitId;
            newPharmaQueue.pharmaState = PharmaQueueState.WaitPack.getCode();
            dbBackend.txProc(b -> b.enterPharmaQueue(newPharmaQueue));
        }
    }

    public void backendEnterCharge(ChargeDTO charge) {
        dbBackend.txProc(backend -> backend.enterCharge(charge));
    }

    public void enterCharge(int visitId, int charge) {
        ChargeDTO newCharge = new ChargeDTO();
        newCharge.visitId = visitId;
        newCharge.charge = charge;
        dbBackend.txProc(backend -> backend.enterCharge(newCharge));
        chargeModifiedHook(visitId);
    }

    public void updateCharge(int visitId, int charge) {
        ChargeDTO newCharge = new ChargeDTO();
        newCharge.visitId = visitId;
        newCharge.charge = charge;
        dbBackend.txProc(backend -> backend.updateCharge(newCharge));
        chargeModifiedHook(visitId);
    }

    private void chargeModifiedHook(int visitId) {
        WqueueDTO wq = dbBackend.query(backend -> backend.getWqueue(visitId));
        if (wq == null) {
            WqueueDTO newWqueue = new WqueueDTO();
            newWqueue.visitId = visitId;
            newWqueue.waitState = WqueueWaitState.WaitCashier.getCode();
            dbBackend.txProc(backend -> backend.enterWqueue(newWqueue));
        } else {
            WqueueDTO newWqueue = WqueueDTO.copy(wq);
            newWqueue.waitState = WqueueWaitState.WaitCashier.getCode();
            dbBackend.txProc(backend -> backend.updateWqueue(newWqueue));
        }
    }

    public void enterDrug(DrugDTO drug) {
        DrugWithAttrDTO drugWithAttr = new DrugWithAttrDTO();
        drugWithAttr.drug = drug;
        drugWithAttr.attr = null;
        enterDrugWithAttr(drugWithAttr);
    }

    public void updateDrug(DrugDTO drug){
        DrugAttrDTO attr = dbBackend.query(backend -> backend.getDrugAttr(drug.drugId));
        DrugWithAttrDTO drugWithAttr = new DrugWithAttrDTO();
        drugWithAttr.drug = drug;
        drugWithAttr.attr = attr;
        updateDrugWithAttr(drugWithAttr);
    }

    public void enterDrugWithAttr(DrugWithAttrDTO drugWithAttr) {
        dbBackend.txProc(backend -> {
            DrugDTO drug = drugWithAttr.drug;
            backend.enterDrug(drug);
            if( drugWithAttr.attr != null ){
                drugWithAttr.attr.drugId = drug.drugId;
                backend.enterDrugAttr(drugWithAttr.attr);
            }
        });
        DrugDTO drug = drugWithAttr.drug;
        if( drug.prescribed == 0 ) {
            WqueueDTO wq = dbBackend.query(backend -> backend.getWqueue(drug.visitId));
            if (wq != null) {
                if (wq.waitState == WqueueWaitState.WaitCashier.getCode() ||
                        wq.waitState == WqueueWaitState.WaitDrug.getCode()) {
                    PharmaQueueDTO pharmaQueue = dbBackend.query(backend -> backend.getPharmaQueue(drug.visitId));
                    if( pharmaQueue == null ){
                        PharmaQueueDTO newPharmaQueue = new PharmaQueueDTO();
                        newPharmaQueue.visitId = drug.visitId;
                        newPharmaQueue.pharmaState = PharmaQueueState.WaitPack.getCode();
                        dbBackend.txProc(backend -> backend.enterPharmaQueue(newPharmaQueue));
                    }
                }
            }
        }
    }

    public void updateDrugWithAttr(DrugWithAttrDTO drugWithAttr){
        DrugDTO drug = drugWithAttr.drug;
        DrugAttrDTO attr = drugWithAttr.attr;
        DrugAttrDTO prevAttr = dbBackend.query(backend -> backend.getDrugAttr(drug.drugId));
        dbBackend.txProc(backend -> {
            backend.updateDrug(drug);
            if( attr == null || DrugAttrDTO.isEmpty(attr) ){
                if( prevAttr != null ){
                    backend.deleteDrugAttr(drug.drugId);
                }
            } else {
                if( prevAttr == null ){
                    backend.enterDrugAttr(attr);
                } else {
                    backend.updateDrugAttr(attr);
                }
            }
        });
        WqueueDTO wq = dbBackend.query(backend -> backend.getWqueue(drug.visitId));
        if (wq != null) {
            if (wq.waitState == WqueueWaitState.WaitCashier.getCode() ||
                    wq.waitState == WqueueWaitState.WaitDrug.getCode()) {
                int unprescribed = dbBackend.query(backend -> backend.countUnprescribedDrug(drug.visitId));
                if( unprescribed > 0 ){
                    PharmaQueueDTO pharmaQueue = dbBackend.query(backend -> backend.getPharmaQueue(drug.visitId));
                    if( pharmaQueue == null ){
                        PharmaQueueDTO newPharmaQueue = new PharmaQueueDTO();
                        newPharmaQueue.visitId = drug.visitId;
                        newPharmaQueue.pharmaState = PharmaQueueState.WaitPack.getCode();
                        dbBackend.txProc(backend -> backend.enterPharmaQueue(newPharmaQueue));
                    } else {
                        if( pharmaQueue.pharmaState == PharmaQueueState.PackDone.getCode() ){
                            PharmaQueueDTO updated = PharmaQueueDTO.copy(pharmaQueue);
                            updated.pharmaState = PharmaQueueState.WaitPack.getCode();
                            dbBackend.txProc(backend -> backend.updatePharmaQueue(updated));
                        }
                    }
                } else {
                    // Do nothing!
                    // PharmaQueue is deleted explicitly by calling deletePharmaQueue (from pharma program)
                }
            }
        }
    }

    public void deleteDrug(int drugId){
        DrugAttrDTO attr = dbBackend.query(backend -> backend.getDrugAttr(drugId));
        dbBackend.txProc(backend -> {
            backend.deleteDrug(drugId);
            if( attr != null ){
                backend.deleteDrugAttr(drugId);
            }
        });
    }

    public void batchDeleteDrugs(List<Integer> drugIds) {
       for(Integer drugId: drugIds){
           deleteDrug(drugId);
       }
    }

}
