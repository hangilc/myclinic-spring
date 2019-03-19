package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.*;
import jp.chang.myclinic.dto.ShahokokuhoDTO;

public interface ShahokokuhoTableInterface
    extends TableInterface<ShahokokuhoDTO>,
        Query.Projector<ShahokokuhoDTO>,
        SqlTranslator.TableInfo {}
