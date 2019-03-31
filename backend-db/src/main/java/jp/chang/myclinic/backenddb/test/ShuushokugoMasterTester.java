package jp.chang.myclinic.backenddb.test;

import jp.chang.myclinic.backenddb.Backend;
import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.backenddb.test.annotation.DbTest;
import jp.chang.myclinic.dto.ShuushokugoMasterDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

class ShuushokugoMasterTester extends TesterBase {

    ShuushokugoMasterTester(DbBackend dbBackend) {
        super(dbBackend);
    }

    @DbTest
    public void testSearchShuushokugo(Backend backend){
        List<ShuushokugoMasterDTO> result = backend.searchShuushokugoMaster("胸", LocalDate.now());
        //System.out.println(result);
    }
}
