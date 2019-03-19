package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.*;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;

public interface IyakuhinMasterTableInterface
    extends TableInterface<IyakuhinMasterDTO>,
        Query.Projector<IyakuhinMasterDTO>,
        SqlTranslator.TableInfo {}
