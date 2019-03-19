package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.*;
import jp.chang.myclinic.dto.ShinryouAttrDTO;

public interface ShinryouAttrTableInterface
    extends TableInterface<ShinryouAttrDTO>,
        Query.Projector<ShinryouAttrDTO>,
        SqlTranslator.TableInfo {}
