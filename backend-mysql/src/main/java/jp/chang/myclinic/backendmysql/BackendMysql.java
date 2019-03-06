package jp.chang.myclinic.backendmysql;

import jp.chang.myclinic.backend.Backend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BackendMysql extends Backend {

    @Autowired
    public BackendMysql(PersistenceMysql persistence){
        super(persistence);
    }

}
