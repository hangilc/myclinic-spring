package jp.chang.myclinic.backenddb.test;

import jp.chang.myclinic.backenddb.Backend;
import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.backenddb.test.annotation.DbTest;
import jp.chang.myclinic.dto.DiseaseFullDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DiseaseTester extends TesterBase {


    DiseaseTester(DbBackend dbBackend) {
        super(dbBackend);
    }

    @DbTest
    public void testGetDiseaseFull(Backend backend){
        DiseaseFullDTO result = backend.getDiseaseFull(1000);
        System.out.println(result);
    }
}
