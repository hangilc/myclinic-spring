package jp.chang.myclinic.backend.test;

import jp.chang.myclinic.backend.Backend;
import jp.chang.myclinic.backend.test.annotation.DbTest;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class VisitTester extends TesterBase {

    public VisitTester(Backend backend) {
        super(backend);
    }


}
