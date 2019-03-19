package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.*;
import jp.chang.myclinic.dto.ConductShinryouDTO;

public interface ConductShinryouTableInterface
    extends TableInterface<ConductShinryouDTO>,
        Query.Projector<ConductShinryouDTO>,
        SqlTranslator.TableInfo {}
