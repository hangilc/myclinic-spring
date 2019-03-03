package jp.chang.myclinic.backendmock;

import jp.chang.myclinic.backend.Backend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BackendMock extends Backend {

    public BackendMock() {
        super(new PersistenceMock());
    }

}
