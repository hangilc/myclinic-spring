package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.*;
import jp.chang.myclinic.dto.ByoumeiMasterDTO;

public interface ByoumeiMasterTableInterface
    extends TableInterface<ByoumeiMasterDTO>,
        Query.Projector<ByoumeiMasterDTO>,
        SqlTranslator.TableInfo {}
