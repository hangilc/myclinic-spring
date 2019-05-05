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
    public void testSearchIyakuhinMaster(){
        List<IyakuhinMasterDTO> result = dbBackend.query(backend ->
                backend.searchIyakuhinMaster("アムロジン", LocalDate.now()));
        confirm(result.size() > 0);
    }
}
