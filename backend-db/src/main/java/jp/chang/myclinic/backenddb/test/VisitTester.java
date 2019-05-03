package jp.chang.myclinic.backenddb.test;

import jp.chang.myclinic.backenddb.Backend;
import jp.chang.myclinic.backenddb.exception.CannotDeleteVisitSafelyException;
import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.backenddb.test.annotation.DbTest;

import static java.util.stream.Collectors.toList;
import static jp.chang.myclinic.consts.MyclinicConsts.*;

import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.logdto.practicelog.*;

import java.time.LocalDateTime;
import java.util.List;

public class VisitTester extends TesterBase {

    public VisitTester(DbBackend dbBackend) {
        super(dbBackend);
    }

    @DbTest
    public void startVisit() {
        int logIndex = getCurrentPracticeLogIndex();
        PatientDTO patient = mock.pickPatient();
        dbBackend.txProc(backend -> backend.enterPatient(patient));
        class Local {
            private VisitDTO visit;
        }
        Local local = new Local();
        LocalDateTime now = LocalDateTime.now();
        dbBackend.txProc(backend -> local.visit = backend.startVisit(patient.patientId, now));
        final VisitDTO visit = local.visit;
        confirm(visit.patientId == patient.patientId, "patient-id", () -> {
            System.out.println("visit: " + visit);
            System.out.println("patient: " + patient);
        });
        confirm(toSqlDatetime(now).equals(visit.visitedAt), "visited-at", () -> {
            System.out.println("visitedAt: " + visit.visitedAt);
            System.out.println("now: " + now);
        });
        List<WqueueDTO> wqueueList = dbBackend.query(Backend::listWqueue)
                .stream()
                .filter(wq -> wq.visitId == visit.visitId)
                .collect(toList());
        confirm(wqueueList.size() == 1);
        confirm(wqueueList.get(0).visitId == visit.visitId &&
                wqueueList.get(0).waitState == WqueueStateWaitExam);
        List<PracticeLogDTO> logs = getPracticeLogList(logIndex, log -> {
            if (log.isVisitCreated()) {
                return log.asVisitCreated().created.visitId == visit.visitId;
            }
            if (log.isWqueueCreated()) {
                return log.asWqueueCreated().created.visitId == visit.visitId;
            }
            return false;
        });
        confirm(logs.size() == 2);
        confirm(logs.get(0).isVisitCreated() && logs.get(0).asVisitCreated().created.visitId == visit.visitId);
        confirm(logs.get(1).isWqueueCreated() && logs.get(1).asWqueueCreated().created.visitId == visit.visitId);
    }

    @DbTest
    public void startExam() {
        int logIndex = getCurrentPracticeLogIndex();
        PatientDTO patient = mock.pickPatient();
        dbBackend.txProc(backend -> backend.enterPatient(patient));
        VisitDTO visit = dbBackend.query(backend ->
                backend.startVisit(patient.patientId, LocalDateTime.now())
        );
        dbBackend.txProc(backend -> backend.startExam(visit.visitId));
        List<WqueueDTO> wqueueList = dbBackend.query(backend -> backend.listWqueue().stream()
                .filter(wq -> wq.visitId == visit.visitId).collect(toList()));
        confirm(wqueueList.size() == 1);
        WqueueDTO wqueue = wqueueList.get(0);
        confirmNotNull(wqueue);
        confirm(wqueue.waitState == WqueueStateInExam && wqueue.visitId == visit.visitId);
        List<PracticeLogDTO> logs = getPracticeLogList(logIndex, log -> {
            if (log.isWqueueUpdated()) {
                return log.asWqueueUpdated().updated.visitId == visit.visitId;
            }
            return false;
        });
        confirm(logs.size() == 1);
        WqueueDTO prev = logs.get(0).asWqueueUpdated().prev;
        WqueueDTO updated = logs.get(0).asWqueueUpdated().updated;
        confirm(prev.visitId == visit.visitId && prev.waitState == WqueueStateWaitExam);
        confirm(updated.visitId == visit.visitId && updated.waitState == WqueueStateInExam);
    }

    @DbTest
    public void endExam() {
        int logIndex = getCurrentPracticeLogIndex();
        PatientDTO patient = mock.pickPatient();
        dbBackend.txProc(backend -> backend.enterPatient(patient));
        VisitDTO visit = dbBackend.query(backend -> backend.startVisit(patient.patientId, LocalDateTime.now()));
        int chargeValue = 570;
        dbBackend.txProc(backend -> backend.startExam(visit.visitId));
        dbBackend.txProc(backend -> backend.endExam(visit.visitId, chargeValue));
        List<WqueueDTO> wqueueList = dbBackend.query(backend -> backend.listWqueue().stream()
                .filter(wq -> wq.visitId == visit.visitId).collect(toList()));
        confirm(wqueueList.size() == 1);
        confirm(wqueueList.get(0).waitState == WqueueStateWaitCashier && wqueueList.get(0).visitId == visit.visitId);
        ChargeDTO charge = dbBackend.query(backend -> backend.getCharge(visit.visitId));
        confirmNotNull(charge);
        confirm(charge.charge == chargeValue && charge.visitId == visit.visitId);
        int unprescribed = dbBackend.query(backend -> backend.countUnprescribedDrug(visit.visitId));
        confirm(unprescribed == 0);
        PharmaQueueDTO pharmaQueue = dbBackend.query(backend -> backend.getPharmaQueue(visit.visitId));
        confirm(pharmaQueue == null);
        confirmSingleLog(logIndex, log -> log.getWqueueUpdated()
                .map(wqueueUpdated -> {
                    WqueueDTO prev = wqueueUpdated.prev;
                    WqueueDTO updated = wqueueUpdated.updated;
                    return prev.visitId == visit.visitId &&
                            prev.waitState == WqueueStateInExam &&
                            updated.visitId == visit.visitId &&
                            updated.waitState == WqueueStateWaitCashier;
                })
                .orElse(false)
        );
        confirmSingleLog(logIndex, log -> log.getChargeCreated()
                .map(chargeCreated -> {
                    ChargeDTO created = chargeCreated.created;
                    return created.visitId == visit.visitId && created.charge == chargeValue;
                }).orElse(false)
        );
    }

    @DbTest
    public void testGetVisitFull() {
        PatientDTO patient = mock.pickPatient();
        dbBackend.txProc(backend -> backend.enterPatient(patient));
        VisitDTO visit = dbBackend.query(backend -> backend.startVisit(patient.patientId, LocalDateTime.now()));
        dbBackend.txProc(backend -> backend.startExam(visit.visitId));
        dbBackend.txProc(backend -> backend.endExam(visit.visitId, 0));
        VisitFullDTO visitFull = dbBackend.query(backend -> backend.getVisitFull(visit.visitId));
        confirm(visitFull.visit.visitId == visit.visitId);
        confirm(visitFull.texts.size() == 0);
        confirm(visitFull.drugs.size() == 0);
        confirm(visitFull.shinryouList.size() == 0);
        confirm(visitFull.conducts.size() == 0);
    }

    @DbTest
    public void testGetVisitFull2() {
        PatientDTO patient = mock.pickPatient();
        dbBackend.txProc(backend -> backend.enterPatient(patient));
        VisitDTO visit = dbBackend.query(backend -> backend.startVisit(patient.patientId, LocalDateTime.now()));
        dbBackend.txProc(backend -> backend.startExam(visit.visitId));
        dbBackend.txProc(backend -> backend.endExam(visit.visitId, 0));
        VisitFull2DTO visitFull = dbBackend.query(backend -> backend.getVisitFull2(visit.visitId));
        confirm(visitFull.visit.visitId == visit.visitId);
        confirm(visitFull.texts.size() == 0);
        confirm(visitFull.drugs.size() == 0);
        confirm(visitFull.shinryouList.size() == 0);
        confirm(visitFull.conducts.size() == 0);
        confirm(visitFull.charge.charge == 0);
        confirm(visitFull.hoken.shahokokuho == null);
        confirm(visitFull.hoken.koukikourei == null);
        confirm(visitFull.hoken.roujin == null);
        confirm(visitFull.hoken.kouhi1 == null);
        confirm(visitFull.hoken.kouhi2 == null);
        confirm(visitFull.hoken.kouhi3 == null);
    }

    @DbTest
    public void testDeleteVisit() {
        PatientDTO patient = mock.pickPatient();
        dbBackend.txProc(b -> b.enterPatient(patient));
        VisitDTO visit = dbBackend.query(b -> b.startVisit(patient.patientId, LocalDateTime.now()));
        int logIndex = getCurrentPracticeLogIndex();
        dbBackend.txProc(b -> b.deleteVisitSafely(visit.visitId));
        confirm(dbBackend.query(b -> b.getVisit(visit.visitId)) == null);
        confirm(dbBackend.query(b -> b.getWqueue(visit.visitId)) == null);
        confirm(dbBackend.query(b -> b.getPharmaQueue(visit.visitId)) == null);
        confirmSingleLog(logIndex, log -> log.getWqueueDeleted()
                .map(wqueueDeleted -> wqueueDeleted.deleted.visitId == visit.visitId)
                .orElse(false));
        confirmSingleLog(logIndex, log -> log.getVisitDeleted()
                .map(visitDeleted -> visitDeleted.deleted.visitId == visit.visitId)
                .orElse(false));
    }

    @DbTest
    public void testDeleteVisitFails(){
        PatientDTO patient = mock.pickPatient();
        dbBackend.txProc(b -> b.enterPatient(patient));
        VisitDTO visit = dbBackend.query(b -> b.startVisit(patient.patientId, LocalDateTime.now()));
        TextDTO text = new TextDTO();
        text.visitId = visit.visitId;
        text.content = "Not to be deleted.";
        dbBackend.txProc(b -> b.enterText(text));
        class Local {
            private boolean catched;
        }
        Local local = new Local();
        dbBackend.txProc(b -> {
            try {
                b.deleteVisitSafely(visit.visitId);
            } catch(CannotDeleteVisitSafelyException e){
                local.catched = true;
            }
        });
        confirm(local.catched);
        confirm(dbBackend.query(b -> b.getVisit(visit.visitId) != null));
        confirm(dbBackend.query(b -> b.getText(text.textId) != null));
    }

    @DbTest
    public void testTodaysVisit(){
        PatientDTO patient = mock.pickPatient();
        dbBackend.txProc(b -> b.enterPatient(patient));
        VisitDTO visit = dbBackend.query(b -> b.startVisit(patient.patientId, LocalDateTime.now()));
        List<VisitPatientDTO> visits = dbBackend.query(Backend::listTodaysVisit);
        confirm(visits.stream().filter(vp -> vp.visit.visitId == visit.visitId).count() == 1);
    }

}
