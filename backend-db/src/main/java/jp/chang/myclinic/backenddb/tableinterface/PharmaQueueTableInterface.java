package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.*;
import jp.chang.myclinic.dto.PharmaQueueDTO;

public interface PharmaQueueTableInterface
    extends TableInterface<PharmaQueueDTO>,
        Query.Projector<PharmaQueueDTO>,
        SqlTranslator.TableInfo {}
