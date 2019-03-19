package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.*;
import jp.chang.myclinic.dto.IntraclinicTagPostDTO;

public interface IntraclinicTagPostTableInterface
    extends TableInterface<IntraclinicTagPostDTO>,
        Query.Projector<IntraclinicTagPostDTO>,
        SqlTranslator.TableInfo {}
