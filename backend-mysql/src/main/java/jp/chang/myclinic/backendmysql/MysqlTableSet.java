package jp.chang.myclinic.backendmysql;

import jp.chang.myclinic.backenddb.Query;
import jp.chang.myclinic.backenddb.TableSet;
import jp.chang.myclinic.backendmysql.table.*;

public class MysqlTableSet {

    private MysqlTableSet(){}

    public static TableSet create(Query query){
        TableSet ts = new TableSet();
        ts.byoumeiMasterTable = new ByoumeiMasterTable(query);
        ts.chargeTable = new ChargeTable(query);
        ts.conductTable = new ConductTable(query);
        ts.conductDrugTable = new ConductDrugTable(query);
        ts.conductKizaiTable = new ConductKizaiTable(query);
        ts.conductShinryouTable = new ConductShinryouTable(query);
        ts.diseaseTable = new DiseaseTable(query);
        ts.diseaseAdjTable = new DiseaseAdjTable(query);
        ts.drugTable = new DrugTable(query);
        ts.drugAttrTable = new DrugAttrTable(query);
        ts.gazouLabelTable = new GazouLabelTable(query);
        ts.hotlineTable = new HotlineTable(query);
        ts.intraclinicCommentTable = new IntraclinicCommentTable(query);
        ts.intraclinicPostTable = new IntraclinicPostTable(query);
        ts.intraclinicTagTable = new IntraclinicTagTable(query);
        ts.intraclinicTagPostTable = new IntraclinicTagPostTable(query);
        ts.iyakuhinMasterTable = new IyakuhinMasterTable(query);
        ts.kizaiMasterTable = new KizaiMasterTable(query);
        ts.kouhiTable = new KouhiTable(query);
        ts.koukikoureiTable = new KoukikoureiTable(query);
        ts.patientTable = new PatientTable(query);
        ts.paymentTable = new PaymentTable(query);
        ts.pharmaDrugTable = new PharmaDrugTable(query);
        ts.pharmaQueueTable = new PharmaQueueTable(query);
        ts.practiceLogTable = new PracticeLogTable(query);
        ts.prescExampleTable = new PrescExampleTable(query);
        ts.roujinTable = new RoujinTable(query);
        ts.shahokokuhoTable = new ShahokokuhoTable(query);
        ts.shinryouTable = new ShinryouTable(query);
        ts.shinryouAttrTable = new ShinryouAttrTable(query);
        ts.shinryouMasterTable = new ShinryouMasterTable(query);
        ts.shoukiTable = new ShoukiTable(query);
        ts.shuushokugoMasterTable = new ShuushokugoMasterTable(query);
        ts.textTable = new TextTable(query);
        ts.visitTable = new VisitTable(query);
        ts.wqueueTable = new WqueueTable(query);

        ts.dialect = new MysqlDialect();
        return ts;
    }

}
