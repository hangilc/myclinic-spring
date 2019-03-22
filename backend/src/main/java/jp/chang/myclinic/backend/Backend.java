package jp.chang.myclinic.backend;

import jp.chang.myclinic.backend.annotation.BackendAsyncClientOption;
import jp.chang.myclinic.backend.annotation.BackendPrivate;
import jp.chang.myclinic.consts.MyclinicConsts;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.logdto.HotlineLogger;
import jp.chang.myclinic.logdto.PracticeLogger;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;
import jp.chang.myclinic.util.DateTimeUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

public class Backend {

    private Persistence db;
    private PracticeLogger practiceLogger;
    private HotlineLogger hotlineLogger;

    public Backend(Persistence db){
        this.db = db;
        this.practiceLogger = new PracticeLogger();
        practiceLogger.setSaver(this::enterPracticeLog);
        this.hotlineLogger = new HotlineLogger();
    }

    @BackendPrivate
    public void setPracticeLogPublisher(Consumer<String> publisher){
        practiceLogger.setPublisher(publisher::accept);
    }

    @BackendPrivate
    public void setHotlineLogPublisher(Consumer<String> publisher){
        hotlineLogger.setHotlineLogPublisher(publisher::accept);
    }

    public void enterPatient(PatientDTO patient){
        db.enterPatient(patient);
        practiceLogger.logPatientCreated(patient);
    }

    public PatientDTO getPatient(int patientId){
        return db.getPatient(patientId);
    }

    public void updatePatient(PatientDTO patient){
        PatientDTO prev = db.getPatient(patient.patientId);
        db.updatePatient(patient);
        practiceLogger.logPatientUpdated(prev, patient);
    }

    private void enterVisit(VisitDTO visit){
        db.enterVisit(visit);
        practiceLogger.logVisitCreated(visit);
    }

    @BackendAsyncClientOption(convertLocalDateTime = true, composeResult = "api::getVisit")
    public VisitDTO startVisit(int patientId, LocalDateTime at){
        LocalDate atDate = at.toLocalDate();
        VisitDTO visitDTO = new VisitDTO();
        visitDTO.patientId = patientId;
        visitDTO.visitedAt = DateTimeUtil.toSqlDateTime(at);
        {
            List<ShahokokuhoDTO> list = db.findAvailableShahokokuho(patientId, atDate);
            if (list.size() == 0) {
                visitDTO.shahokokuhoId = 0;
            } else {
                visitDTO.shahokokuhoId = list.get(0).shahokokuhoId;
            }
        }
        {
            List<KoukikoureiDTO> list = db.findAvailableKoukikourei(patientId, atDate);
            if (list.size() == 0) {
                visitDTO.koukikoureiId = 0;
            } else {
                visitDTO.koukikoureiId = list.get(0).koukikoureiId;
            }
        }
        {
            List<RoujinDTO> list = db.findAvailableRoujin(patientId, atDate);
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
            List<KouhiDTO> list = db.findAvailableKouhi(patientId, atDate);
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
        enterVisit(visitDTO);
        WqueueDTO wqueueDTO = new WqueueDTO();
        wqueueDTO.visitId = visitDTO.visitId;
        wqueueDTO.waitState = MyclinicConsts.WqueueStateWaitExam;
        enterWqueue(wqueueDTO);
        return visitDTO;
    }

    private void enterWqueue(WqueueDTO wqueue){
        db.enterWqueue(wqueue);
        practiceLogger.logWqueueCreated(wqueue);
    }

    public HokenDTO getHoken(int visitId) {
        VisitDTO visitDTO = db.getVisit(visitId);
        HokenDTO hokenDTO = new HokenDTO();
        if (visitDTO.shahokokuhoId > 0) {
            hokenDTO.shahokokuho = db.getShahokokuho(visitDTO.shahokokuhoId);
        }
        if (visitDTO.koukikoureiId > 0) {
            hokenDTO.koukikourei = db.getKoukikourei(visitDTO.koukikoureiId);
        }
        if (visitDTO.roujinId > 0) {
            hokenDTO.roujin = db.getRoujin(visitDTO.roujinId);
        }
        if (visitDTO.kouhi1Id > 0) {
            hokenDTO.kouhi1 = db.getKouhi(visitDTO.kouhi1Id);
        }
        if (visitDTO.kouhi2Id > 0) {
            hokenDTO.kouhi2 = db.getKouhi(visitDTO.kouhi2Id);
        }
        if (visitDTO.kouhi3Id > 0) {
            hokenDTO.kouhi3 = db.getKouhi(visitDTO.kouhi3Id);
        }
        return hokenDTO;
    }

    public List<ShinryouAttrDTO> batchGetShinryouAttr(List<Integer> shinryouIds){
        return db.batchGetShinryouAttr(shinryouIds);
    }

    public List<DrugAttrDTO> batchGetDrugAttr(List<Integer> drugIds){
        return db.batchGetDrugAttr(drugIds);
    }

    public List<ShoukiDTO> batchGetShouki(List<Integer> visitIds){
        return db.batchGetShouki(visitIds);
    }

    public VisitDTO getVisit(int visitId){
        return db.getVisit(visitId);
    }

    public void enterText(TextDTO text){
        db.enterText(text);
        practiceLogger.logTextCreated(text);
    }

    public TextDTO getText(int textId){
        return db.getText(textId);
    }

    public void updateText(TextDTO text){
        TextDTO prev = getText(text.textId);
        db.updateText(text);
        practiceLogger.logTextUpdated(prev, text);
    }

    public void deleteText(int textId){
        TextDTO text = getText(textId);
        db.deleteText(textId);
        practiceLogger.logTextDeleted(text);
    }

    public List<TextDTO> listText(int visitId){
        return db.listText(visitId);
    }

    private void enterPracticeLog(PracticeLogDTO practiceLog){
        db.enterPracticeLog(practiceLog);
    }

}
