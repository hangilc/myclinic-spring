package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.*;
import jp.chang.myclinic.dto.PharmaDrugDTO;

public interface PharmaDrugTableInterface
    extends TableInterface<PharmaDrugDTO>,
        Query.Projector<PharmaDrugDTO>,
        SqlTranslator.TableInfo {}
