package jp.chang.myclinic.backenddb.test;

import jp.chang.myclinic.backenddb.Backend;
import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.backenddb.test.annotation.DbTest;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

class IyakuhinMasterTester extends TesterBase {


    IyakuhinMasterTester(DbBackend dbBackend) {
        super(dbBackend);
    }

    @DbTest
    public void testSearchIyakuhinMaster(Backend backend){
        List<IyakuhinMasterDTO> result = backend.searchIyakuhinMaster("アムロジン", LocalDate.now());
        //System.out.println(result);
    }
}
