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
        int visitId = db.enterVisit(visitDTO);
        visitDTO.visitId = visitId;
        WqueueDTO wqueueDTO = new WqueueDTO();
        wqueueDTO.visitId = visitId;
        wqueueDTO.waitState = MyclinicConsts.WqueueStateWaitExam;
        dbGateway.enterWqueue(wqueueDTO);
        return visitId;
    }

}
