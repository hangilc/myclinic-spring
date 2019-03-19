package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.*;
import jp.chang.myclinic.dto.IntraclinicPostDTO;

public interface IntraclinicPostTableInterface
    extends TableInterface<IntraclinicPostDTO>,
        Query.Projector<IntraclinicPostDTO>,
        SqlTranslator.TableInfo {}
