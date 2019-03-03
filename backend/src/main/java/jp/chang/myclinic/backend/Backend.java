package jp.chang.myclinic.backend;

import jp.chang.myclinic.consts.MyclinicConsts;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Backend {

    private Persistence db;

    public Backend(Persistence db){
        this.db = db;
    }

    public int enterPatient(PatientDTO patient){
        return db.getPatientPersistence().enterPatient(patient);
    }

    public PatientDTO getPatient(int patientId){
        return db.getPatientPersistence().getPatient(patientId);
    }

    public int startVisit(int patientId, LocalDateTime at){
        LocalDate atDate = at.toLocalDate();
        VisitDTO visitDTO = new VisitDTO();
        visitDTO.patientId = patientId;
        visitDTO.visitedAt = DateTimeUtil.toSqlDateTime(at);
        {
            List<ShahokokuhoDTO> list = db.getShahokokuhoPersistence().findAvailableShahokokuho(patientId, atDate);
            if (list.size() == 0) {
                visitDTO.shahokokuhoId = 0;
            } else {
                visitDTO.shahokokuhoId = list.get(0).shahokokuhoId;
            }
        }
        {
            List<KoukikoureiDTO> list = db.getKoukikoureiPersistence().findAvailableKoukikourei(patientId, atDate);
            if (list.size() == 0) {
                visitDTO.koukikoureiId = 0;
            } else {
                visitDTO.koukikoureiId = list.get(0).koukikoureiId;
            }
        }
        {
            List<RoujinDTO> list = db.getRoujinPersistence().findAvailableRoujin(patientId, atDate);
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
            List<KouhiDTO> list = db.getKouhiPersistence().findAvailableKouhi(patientId, atDate);
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
        int visitId = db.getVisitPersistence().enterVisit(visitDTO);
        visitDTO.visitId = visitId;
        WqueueDTO wqueueDTO = new WqueueDTO();
        wqueueDTO.visitId = visitId;
        wqueueDTO.waitState = MyclinicConsts.WqueueStateWaitExam;
        db.getWqueuePersistence().enterWqueue(wqueueDTO);
        return visitId;
    }

    public HokenDTO getHoken(int visitId) {
        VisitDTO visitDTO = db.getVisitPersistence().getVisit(visitId);
        HokenDTO hokenDTO = new HokenDTO();
        if (visitDTO.shahokokuhoId > 0) {
            hokenDTO.shahokokuho = db.getShahokokuhoPersistence().getShahokokuho(visitDTO.shahokokuhoId);
        }
        if (visitDTO.koukikoureiId > 0) {
            hokenDTO.koukikourei = db.getKoukikoureiPersistence().getKoukikourei(visitDTO.koukikoureiId);
        }
        if (visitDTO.roujinId > 0) {
            hokenDTO.roujin = db.getRoujinPersistence().getRoujin(visitDTO.roujinId);
        }
        if (visitDTO.kouhi1Id > 0) {
            hokenDTO.kouhi1 = db.getKouhiPersistence().getKouhi(visitDTO.kouhi1Id);
        }
        if (visitDTO.kouhi2Id > 0) {
            hokenDTO.kouhi2 = db.getKouhiPersistence().getKouhi(visitDTO.kouhi2Id);
        }
        if (visitDTO.kouhi3Id > 0) {
            hokenDTO.kouhi3 = db.getKouhiPersistence().getKouhi(visitDTO.kouhi3Id);
        }
        return hokenDTO;
    }

    public List<ShinryouAttrDTO> batchGetShinryouAttr(List<Integer> shinryouIds){
        return db.getShinryouPersistence().batchGetShinryouAttr(shinryouIds);
    }

    public List<DrugAttrDTO> batchGetDrugAttr(List<Integer> drugIds){
        return db.getDrugPersistence().batchGetDrugAttr(drugIds);
    }

    public List<ShoukiDTO> batchGetShouki(List<Integer> visitIds){
        return db.getVisitPersistence().batchGetShouki(visitIds);
    }

    public VisitDTO getVisit(int visitId){
        return db.getVisitPersistence().getVisit(visitId);
    }

    public int enterText(TextDTO text){
        return db.getTextPersistence().enterText(text);
    }

    public TextDTO getText(int textId){
        return db.getTextPersistence().getText(textId);
    }

    public void updateText(TextDTO text){
        db.getTextPersistence().updateText(text);
    }

    public void deleteText(int textId){
        db.getTextPersistence().deleteText(textId);
    }


}
