package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.*;
import jp.chang.myclinic.dto.KizaiMasterDTO;

public interface KizaiMasterTableInterface
    extends TableInterface<KizaiMasterDTO>,
        Query.Projector<KizaiMasterDTO>,
        SqlTranslator.TableInfo {}
