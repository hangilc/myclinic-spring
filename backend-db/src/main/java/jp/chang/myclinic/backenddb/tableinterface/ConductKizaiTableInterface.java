package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.*;
import jp.chang.myclinic.dto.ConductKizaiDTO;

public interface ConductKizaiTableInterface
    extends TableInterface<ConductKizaiDTO>,
        Query.Projector<ConductKizaiDTO>,
        SqlTranslator.TableInfo {}
