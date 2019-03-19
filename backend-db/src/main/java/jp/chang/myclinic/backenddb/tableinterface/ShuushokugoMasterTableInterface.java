package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.*;
import jp.chang.myclinic.dto.ShuushokugoMasterDTO;

public interface ShuushokugoMasterTableInterface
    extends TableInterface<ShuushokugoMasterDTO>,
        Query.Projector<ShuushokugoMasterDTO>,
        SqlTranslator.TableInfo {}
