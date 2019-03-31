package jp.chang.myclinic.backenddb.test;

import jp.chang.myclinic.backenddb.Backend;
import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.backenddb.test.annotation.DbTest;
import jp.chang.myclinic.dto.ByoumeiMasterDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

class ByoumeiMasterTester extends TesterBase {

    ByoumeiMasterTester(DbBackend dbBackend) {
        super(dbBackend);
    }

    @DbTest
    public void testSearchByoumei(Backend backend){
        List<ByoumeiMasterDTO> result = backend.searchByoumeiMaster("高血圧", LocalDate.now());
        //System.out.println(result);
    }
}
