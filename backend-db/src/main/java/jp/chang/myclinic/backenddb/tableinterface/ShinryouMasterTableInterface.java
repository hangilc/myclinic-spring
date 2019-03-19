package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.*;
import jp.chang.myclinic.dto.ShinryouMasterDTO;

public interface ShinryouMasterTableInterface
    extends TableInterface<ShinryouMasterDTO>,
        Query.Projector<ShinryouMasterDTO>,
        SqlTranslator.TableInfo {}
