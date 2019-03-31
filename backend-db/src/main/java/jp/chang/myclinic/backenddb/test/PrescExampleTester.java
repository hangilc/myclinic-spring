package jp.chang.myclinic.backenddb.test;

import jp.chang.myclinic.backenddb.Backend;
import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.backenddb.test.annotation.DbTest;
import jp.chang.myclinic.dto.PrescExampleFullDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

class PrescExampleTester extends TesterBase {

    PrescExampleTester(DbBackend dbBackend) {
        super(dbBackend);
    }

    @DbTest
    public void testSearchPrescExample(Backend backend){
        List<PrescExampleFullDTO> result = backend.searchPrescExample("アムロ");
        //System.out.println(result);
    }

    @DbTest
    public void tesListAllPrescExample(Backend backend){
        List<PrescExampleFullDTO> result = backend.listAllPrescExample();
        //System.out.println(result);
    }
}
