package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.*;
import jp.chang.myclinic.dto.GazouLabelDTO;

public interface GazouLabelTableInterface
    extends TableInterface<GazouLabelDTO>,
        Query.Projector<GazouLabelDTO>,
        SqlTranslator.TableInfo {}
