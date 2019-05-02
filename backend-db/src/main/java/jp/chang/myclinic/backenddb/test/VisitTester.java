package jp.chang.myclinic.backenddb.test;

import jp.chang.myclinic.backenddb.Backend;
import jp.chang.myclinic.backenddb.CannotDeleteVisitSafelyException;
import jp.chang.myclinic.backenddb.DB;
import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.backenddb.test.annotation.DbTest;
import static jp.chang.myclinic.consts.MyclinicConsts.*;
import jp.chang.myclinic.consts.MyclinicConsts;
import jp.chang.myclinic.consts.WqueueWaitState;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.logdto.practicelog.*;

import java.time.LocalDateTime;
import java.util.List;

public class VisitTester extends TesterBase {

    public VisitTester(DbBackend dbBackend) {
        super(dbBackend);
    }

//    @DbTest
//    public void testStartVisit(DbBackend dbBackend){
//        int serialId = dbBackend.query(Backend::getLastPracticeLogId);
//        dbBackend.txProc(backend -> {
//            VisitDTO visit = backend.startVisit(patient1.patientId, LocalDateTime.now());
//            WqueueDTO wqueue = backend.listWqueue().stream().filter(wq -> wq.visitId == visit.visitId).findFirst()
//                    .orElseThrow(() -> new RuntimeException("Cannot find wqueue"));
//            confirm(wqueue.visitId == visit.visitId && wqueue.waitState == MyclinicConsts.WqueueStateWaitExam);
////            List<PracticeLogDTO> logs = backend.listPracticeLogSince(serialId);
////            confirm(logs.size() == 2);
////            PracticeLogDTO vlog = logs.get(0);
////            confirm(vlog.isVisitCreated());
////            PracticeLogDTO wlog = logs.get(1);
////            confirm(wlog.isWqueueCreated());
////            WqueueCreated wcreated = wlog.asWqueueCreated();
////            confirm(wcreated.created.visitId == visit.visitId);
////            confirm(wcreated.created.waitState == WqueueWaitState.WaitExam.getCode());
//        });
//    }
//
//    @DbTest
//    public void startExam(DbBackend dbBackend){
//        class Local {
//            private VisitDTO visit;
//        }
//        Local local = new Local();
//        int serialId = dbBackend.query(backend -> {
//            local.visit = backend.startVisit(patient1.patientId, LocalDateTime.now());
//            return backend.getLastPracticeLogId();
//        });
//        dbBackend.txProc(backend -> {
//            VisitDTO visit = local.visit;
//            backend.startExam(visit.visitId);
//            WqueueDTO wqueue = backend.listWqueue().stream().filter(wq -> wq.visitId == visit.visitId).findFirst()
//                    .orElseThrow(() -> new RuntimeException("Cannot find wqueue"));
//            confirm(wqueue.visitId == visit.visitId && wqueue.waitState == MyclinicConsts.WqueueStateInExam);
//            List<PracticeLogDTO> logs = backend.listPracticeLogSince(serialId);
////            {
////                PracticeLogDTO log = logs.get(0);
////                confirm(log.isWqueueUpdated());
////                WqueueUpdated body = log.asWqueueUpdated();
////                confirm(body.prev.visitId == visit.visitId &&
////                        body.prev.waitState == MyclinicConsts.WqueueStateWaitExam);
////                confirm(body.updated.visitId == visit.visitId &&
////                        body.updated.waitState == MyclinicConsts.WqueueStateInExam);
////            }
//        });
//    }
//
//    @DbTest
//    public void testEndExam(DbBackend dbBackend){
//        int chargeValue = 570;
//        class Local {
//            private VisitDTO visit;
//        }
//        Local local = new Local();
//        int serialId = dbBackend.query(backend -> {
//            local.visit = backend.startVisit(patient1.patientId, LocalDateTime.now());
//            backend.startExam(local.visit.visitId);
//            return backend.getLastPracticeLogId();
//        });
//        dbBackend.txProc(backend -> {
//            VisitDTO visit = local.visit;
//            backend.endExam(visit.visitId, chargeValue);
//            WqueueDTO wqueue = backend.listWqueue().stream().filter(wq -> wq.visitId == visit.visitId).findFirst()
//                    .orElseThrow(() -> new RuntimeException("Cannot find wqueue"));
//            confirm(wqueue.visitId == visit.visitId && wqueue.waitState == MyclinicConsts.WqueueStateWaitCashier);
//            ChargeDTO charge = backend.getCharge(visit.visitId);
//            confirm(charge.visitId == visit.visitId && charge.charge == chargeValue);
//            confirm(backend.countUnprescribedDrug(visit.visitId) == 0);
//            confirm(backend.getPharmaQueue(visit.visitId) == null);
//            List<PracticeLogDTO> logs = backend.listPracticeLogSince(serialId);
////            {
////                PracticeLogDTO log = logs.get(0);
////                confirm(log.isChargeCreated());
////                ChargeCreated body = log.asChargeCreated();
////                confirm(body.created.visitId == visit.visitId);
////                confirm(body.created.charge == chargeValue);
////            }
////            {
////                PracticeLogDTO log = logs.get(1);
////                confirm(log.isWqueueUpdated());
////                WqueueUpdated body = log.asWqueueUpdated();
////                confirm(body.prev.visitId == visit.visitId && body.prev.waitState == WqueueStateInExam);
////                confirm(body.updated.visitId == visit.visitId && body.updated.waitState == WqueueStateWaitCashier);
////            }
//        });
//    }
//
//    @DbTest
//    public void testGetVisitFull(Backend backend){
//        VisitDTO visit = backend.startVisit(patient1.patientId, LocalDateTime.now());
//        TextDTO text = mock.pickText(visit.visitId);
//        backend.enterText(text);
//        VisitFullDTO visitFull = backend.getVisitFull(visit.visitId);
//        confirm(visitFull.texts.size() == 1);
//        confirm(visitFull.texts.get(0).visitId == visit.visitId);
//        confirm(visitFull.texts.get(0).content.equals(text.content));
//    }
//
//    @DbTest
//    public void testGetVisitFull2(Backend backend){
//
//    }
//
//    @DbTest
//    public void testDeleteVisit(Backend backend){
//        VisitDTO visit = backend.startVisit(patient1.patientId, LocalDateTime.now());
//        int visitId = visit.visitId;
//        int serialId = backend.getLastPracticeLogId();
//        backend.deleteVisitSafely(visitId);
//        confirm(backend.getVisit(visitId) == null);
//        confirm(backend.getWqueue(visitId) == null);
//        confirm(backend.getPharmaQueue(visitId) == null);
//        List<PracticeLogDTO> logs = backend.listPracticeLogSince(serialId);
////        confirm(logs.size() == 2);
////        {
////            PracticeLogDTO log = logs.get(0);
////            confirm(log.isWqueueDeleted());
////            WqueueDeleted body = log.asWqueueDeleted();
////            confirm(body.deleted.visitId == visitId);
////        }
////        {
////            PracticeLogDTO log = logs.get(1);
////            confirm(log.isVisitDeleted());
////            VisitDeleted body = log.asVisitDeleted();
////            confirm(body.deleted.visitId == visitId);
////        }
//    }
//
//    @DbTest
//    public void testDeleteVisitFails(Backend backend){
//        VisitDTO visit = backend.startVisit(patient1.patientId, LocalDateTime.now());
//        TextDTO text = new TextDTO();
//        text.visitId = visit.visitId;
//        text.content = "Not to be deleted.";
//        backend.enterText(text);
//        boolean catched = false;
//        try {
//            backend.deleteVisitSafely(visit.visitId);
//        } catch(CannotDeleteVisitSafelyException e){
//            catched = true;
//        }
//        confirm(catched);
//        confirm(backend.getVisit(visit.visitId) != null);
//        confirm(backend.getText(text.textId) != null);
//    }
//
//    @DbTest
//    public void testTodaysVisit(Backend backend){
//        VisitDTO visit = backend.startVisit(patient1.patientId, LocalDateTime.now());
//        List<VisitPatientDTO> visits = backend.listTodaysVisit();
//        boolean found = false;
//        for(VisitPatientDTO vp: visits){
//            if( vp.visit.visitId == visit.visitId && vp.patient.patientId == patient1.patientId ){
//                found = true;
//                break;
//            }
//        }
//        confirm(found);
//    }
//
//    @DbTest
//    public void testSearchText(Backend backend){
//        TextVisitPageDTO result = backend.searchText(198, "血圧", 0);
//        //System.out.println(result);
//    }
//
//    @DbTest
//    public void testSearchTextGlobally(Backend backend){
//        TextVisitPatientPageDTO result = backend.searchTextGlobally("調子", 0);
//        //System.out.println(result);
//    }
}
