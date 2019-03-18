package jp.chang.myclinic.backenddb;

import jp.chang.myclinic.backenddb.tableinterface.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TableSet {

    public ByoumeiMasterTableInterface byoumeiMasterTable;
    public ChargeTableInterface chargeTable;
    public ConductTableInterface conductTable;
    public ConductDrugTableInterface conductDrugTable;
    public ConductKizaiTableInterface conductKizaiTable;
    public ConductShinryouTableInterface conductShinryouTable;
    public DiseaseTableInterface diseaseTable;
    public DiseaseAdjTableInterface diseaseAdjTable;
    public DrugTableInterface drugTable;
    public DrugAttrTableInterface drugAttrTable;
    public GazouLabelTableInterface gazouLabelTable;
    public HotlineTableInterface hotlineTable;
    public IntraclinicCommentTableInterface intraclinicCommentTable;
    public IntraclinicPostTableInterface intraclinicPostTable;
    public IntraclinicTagTableInterface intraclinicTagTable;
    public IntraclinicTagPostTableInterface intraclinicTagPostTable;
    public IyakuhinMasterTableInterface iyakuhinMasterTable;
    public KizaiMasterTableInterface kizaiMasterTable;
    public KouhiTableInterface kouhiTable;
    public KoukikoureiTableInterface koukikoureiTable;
    public PatientTableInterface patientTable;
    public PaymentTableInterface paymentTable;
    public PharmaDrugTableInterface pharmaDrugTable;
    public PharmaQueueTableInterface pharmaQueueTable;
    public PracticeLogTableInterface practiceLogTable;
    public PrescExampleTableInterface prescExampleTable;
    public RoujinTableInterface roujinTable;
    public ShahokokuhoTableInterface shahokokuhoTable;
    public ShinryouTableInterface shinryouTable;
    public ShinryouAttrTableInterface shinryouAttrTable;
    public ShinryouMasterTableInterface shinryouMasterTable;
    public ShoukiTableInterface shoukiTable;
    public ShuushokugoMasterTableInterface shuushokugoMasterTable;
    public TextTableInterface textTable;
    public VisitTableInterface visitTable;
    public WqueueTableInterface wqueueTable;

}
