package jp.chang.myclinic.backenddb.test;

import jp.chang.myclinic.backenddb.Backend;
import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.backenddb.test.annotation.DbTest;
import jp.chang.myclinic.dto.DrugFullDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

class DrugTester extends TesterBase {

    DrugTester(DbBackend dbBackend) {
        super(dbBackend);
    }

    @DbTest
    public void testGetDrugFull(Backend backend){
        DrugFullDTO result = backend.getDrugFull(1000);
        //System.out.println(result);
    }

    @DbTest
    public void testSearchPrevDrug(Backend backend){
        List<DrugFullDTO> drugs = backend.searchPrevDrug(198);
        //System.out.println(drugs);
    }

    @DbTest
    public void testSearchPrevDrugText(Backend backend){
        List<DrugFullDTO> drugs = backend.searchPrevDrug("ア", 198);
        //System.out.println(drugs);
    }
}
