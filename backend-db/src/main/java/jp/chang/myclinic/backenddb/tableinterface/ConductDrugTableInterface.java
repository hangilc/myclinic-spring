package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.*;
import jp.chang.myclinic.dto.ConductDrugDTO;

public interface ConductDrugTableInterface
    extends TableInterface<ConductDrugDTO>,
        Query.Projector<ConductDrugDTO>,
        SqlTranslator.TableInfo {}
