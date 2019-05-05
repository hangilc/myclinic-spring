package jp.chang.myclinic.backenddb.test;

import jp.chang.myclinic.backenddb.Backend;
import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.backenddb.test.annotation.DbTest;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import jp.chang.myclinic.dto.KizaiMasterDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

class KizaiMasterTester extends TesterBase {

    KizaiMasterTester(DbBackend dbBackend) {
        super(dbBackend);
    }

    @DbTest
    public void testSearchKizaiMaster(){
        List<KizaiMasterDTO> result = dbBackend.query(backend ->
                backend.searchKizaiMaster("切", LocalDate.now()));
        confirm(result.size() > 0);
    }

}
