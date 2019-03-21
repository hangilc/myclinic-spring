package jp.chang.myclinic.backendsqlite;

import jp.chang.myclinic.backend.Backend;
import jp.chang.myclinic.backenddb.DbPersistence;
import jp.chang.myclinic.backenddb.TableSet;
import jp.chang.myclinic.backendsqlite.table.*;

public class SqliteBackend {

    public static Backend create(){
        DbPersistence persist = new DbPersistence(createTableSet());
        return new Backend(persist);
    }

    public static TableSet createTableSet(){
        TableSet ts = new TableSet();
        ts.byoumeiMasterTable = new ByoumeiMasterTable();
        ts.chargeTable = new ChargeTable();
        ts.conductTable = new ConductTable();
        ts.conductDrugTable = new ConductDrugTable();
        ts.conductKizaiTable = new ConductKizaiTable();
        ts.conductShinryouTable = new ConductShinryouTable();
        ts.diseaseTable = new DiseaseTable();
        ts.diseaseAdjTable = new DiseaseAdjTable();
        ts.drugTable = new DrugTable();
        ts.drugAttrTable = new DrugAttrTable();
        ts.gazouLabelTable = new GazouLabelTable();
        ts.hotlineTable = new HotlineTable();
        ts.intraclinicCommentTable = new IntraclinicCommentTable();
        ts.intraclinicPostTable = new IntraclinicPostTable();
        ts.intraclinicTagTable = new IntraclinicTagTable();
        ts.intraclinicTagPostTable = new IntraclinicTagPostTable();
        ts.iyakuhinMasterTable = new IyakuhinMasterTable();
        ts.kizaiMasterTable = new KizaiMasterTable();
        ts.kouhiTable = new KouhiTable();
        ts.koukikoureiTable = new KoukikoureiTable();
        ts.patientTable = new PatientTable();
        ts.paymentTable = new PaymentTable();
        ts.pharmaDrugTable = new PharmaDrugTable();
        ts.pharmaQueueTable = new PharmaQueueTable();
        ts.practiceLogTable = new PracticeLogTable();
        ts.prescExampleTable = new PrescExampleTable();
        ts.roujinTable = new RoujinTable();
        ts.shahokokuhoTable = new ShahokokuhoTable();
        ts.shinryouTable = new ShinryouTable();
        ts.shinryouAttrTable = new ShinryouAttrTable();
        ts.shinryouMasterTable = new ShinryouMasterTable();
        ts.shoukiTable = new ShoukiTable();
        ts.shuushokugoMasterTable = new ShuushokugoMasterTable();
        ts.textTable = new TextTable();
        ts.visitTable = new VisitTable();
        ts.wqueueTable = new WqueueTable();
        return ts;
    }

}
