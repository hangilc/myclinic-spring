package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.*;
import jp.chang.myclinic.dto.IntraclinicTagDTO;

public interface IntraclinicTagTableInterface
    extends TableInterface<IntraclinicTagDTO>,
        Query.Projector<IntraclinicTagDTO>,
        SqlTranslator.TableInfo {}
