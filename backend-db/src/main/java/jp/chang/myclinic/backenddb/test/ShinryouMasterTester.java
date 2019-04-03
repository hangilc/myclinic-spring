package jp.chang.myclinic.backenddb.test;

import jp.chang.myclinic.backenddb.Backend;
import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.backenddb.test.annotation.DbTest;
import jp.chang.myclinic.dto.ShinryouMasterDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

class ShinryouMasterTester extends TesterBase {


    ShinryouMasterTester(DbBackend dbBackend) {
        super(dbBackend);
    }

    @DbTest
    public void testSearchShinryouMaster(Backend backend){
        List<ShinryouMasterDTO> result = backend.searchShinryouMaster("再診", LocalDate.now());
        //System.out.println(result);
    }
}