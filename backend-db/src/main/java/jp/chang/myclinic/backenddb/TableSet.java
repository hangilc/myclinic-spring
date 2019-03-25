package jp.chang.myclinic.backenddb;

import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;

public class TableSet {

    public TableBaseInterface<ByoumeiMasterDTO> byoumeiMasterTable;
    public TableBaseInterface<ChargeDTO> chargeTable;
    public TableBaseInterface<ConductDTO> conductTable;
    public TableBaseInterface<ConductDrugDTO> conductDrugTable;
    public TableBaseInterface<ConductKizaiDTO> conductKizaiTable;
    public TableBaseInterface<ConductShinryouDTO> conductShinryouTable;
    public TableBaseInterface<DiseaseDTO> diseaseTable;
    public TableBaseInterface<DiseaseAdjDTO> diseaseAdjTable;
    public TableBaseInterface<DrugDTO> drugTable;
    public TableBaseInterface<DrugAttrDTO> drugAttrTable;
    public TableBaseInterface<GazouLabelDTO> gazouLabelTable;
    public TableBaseInterface<HotlineDTO> hotlineTable;
    public TableBaseInterface<IntraclinicCommentDTO> intraclinicCommentTable;
    public TableBaseInterface<IntraclinicPostDTO> intraclinicPostTable;
    public TableBaseInterface<IntraclinicTagDTO> intraclinicTagTable;
    public TableBaseInterface<IntraclinicTagPostDTO> intraclinicTagPostTable;
    public TableBaseInterface<IyakuhinMasterDTO> iyakuhinMasterTable;
    public TableBaseInterface<KizaiMasterDTO> kizaiMasterTable;
    public TableBaseInterface<KouhiDTO> kouhiTable;
    public TableBaseInterface<KoukikoureiDTO> koukikoureiTable;
    public TableBaseInterface<PatientDTO> patientTable;
    public TableBaseInterface<PaymentDTO> paymentTable;
    public TableBaseInterface<PharmaDrugDTO> pharmaDrugTable;
    public TableBaseInterface<PharmaQueueDTO> pharmaQueueTable;
    public TableBaseInterface<PracticeLogDTO> practiceLogTable;
    public TableBaseInterface<PrescExampleDTO> prescExampleTable;
    public TableBaseInterface<RoujinDTO> roujinTable;
    public TableBaseInterface<ShahokokuhoDTO> shahokokuhoTable;
    public TableBaseInterface<ShinryouDTO> shinryouTable;
    public TableBaseInterface<ShinryouAttrDTO> shinryouAttrTable;
    public TableBaseInterface<ShinryouMasterDTO> shinryouMasterTable;
    public TableBaseInterface<ShoukiDTO> shoukiTable;
    public TableBaseInterface<ShuushokugoMasterDTO> shuushokugoMasterTable;
    public TableBaseInterface<TextDTO> textTable;
    public TableBaseInterface<VisitDTO> visitTable;
    public TableBaseInterface<WqueueDTO> wqueueTable;

}
