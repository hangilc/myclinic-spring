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
    public void testSearchPrescExample(){
        List<PrescExampleFullDTO> result = dbBackend.query(backend ->
                backend.searchPrescExample("アムロ"));
        confirm(result.size() > 0);
    }

    @DbTest
    public void tesListAllPrescExample(){
        List<PrescExampleFullDTO> result = dbBackend.query(Backend::listAllPrescExample);
        confirm(result.size() > 0);
    }
}
