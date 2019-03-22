package jp.chang.myclinic.backend;

import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;

import java.time.LocalDate;
import java.util.List;

public interface Persistence {

    void enterPatient(PatientDTO patient);

    void updatePatient(PatientDTO patient);

    PatientDTO getPatient(int patientId);

    void enterVisit(VisitDTO visit);

    VisitDTO getVisit(int visitId);

    List<ShahokokuhoDTO> findAvailableShahokokuho(int patientId, LocalDate at);

    ShahokokuhoDTO getShahokokuho(int shahokokuhoId);

    List<KoukikoureiDTO> findAvailableKoukikourei(int patientId, LocalDate at);

    KoukikoureiDTO getKoukikourei(int koukikoureiId);

    List<RoujinDTO> findAvailableRoujin(int patientId, LocalDate at);

    RoujinDTO getRoujin(int roujinId);

    List<KouhiDTO> findAvailableKouhi(int patientId, LocalDate at);

    KouhiDTO getKouhi(int kouhiId);

    void enterWqueue(WqueueDTO wqueue);

    List<ShinryouAttrDTO> batchGetShinryouAttr(List<Integer> shinryouIds);

    List<DrugAttrDTO> batchGetDrugAttr(List<Integer> drugIds);

    List<ShoukiDTO> batchGetShouki(List<Integer> visitIds);

    void enterText(TextDTO text);
    TextDTO getText(int textId);
    void updateText(TextDTO text);
    void deleteText(int textId);
    List<TextDTO> listText(int visitId);

    void enterPracticeLog(PracticeLogDTO practiceLog);
}
