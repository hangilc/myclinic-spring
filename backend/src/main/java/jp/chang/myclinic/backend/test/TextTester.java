package jp.chang.myclinic.backend.test;

import jp.chang.myclinic.backend.Backend;
import jp.chang.myclinic.backend.test.annotation.DbTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextTester extends TesterBase {

    TextTester(Backend backend) {
        super(backend);
    }

    @DbTest
    public void testEnter(){
        
    }
}
