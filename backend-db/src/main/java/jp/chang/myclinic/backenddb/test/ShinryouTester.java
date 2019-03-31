package jp.chang.myclinic.backenddb.test;

import jp.chang.myclinic.backenddb.Backend;
import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.backenddb.test.annotation.DbTest;
import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.dto.ShinryouMasterDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

class ShinryouTester extends TesterBase {


    ShinryouTester(DbBackend dbBackend) {
        super(dbBackend);
    }

    @DbTest
    public void testGetShinryouFull(Backend backend){
        ShinryouFullDTO result = backend.getShinryouFull(2012);
        System.out.println(result);
    }

    @DbTest
    public void testFindShinryouMasterByName(Backend backend){
        Map<String, Integer> map = backend.batchResolveShinryouNames(
                List.of(List.of("初診", "初診", "初診料"), List.of("再診料")), LocalDate.of(2019, 3, 31));
        System.out.println(map);
    }

}
