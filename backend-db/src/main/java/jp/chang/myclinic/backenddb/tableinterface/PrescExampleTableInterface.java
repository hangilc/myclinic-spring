package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.*;
import jp.chang.myclinic.dto.PrescExampleDTO;

public interface PrescExampleTableInterface
    extends TableInterface<PrescExampleDTO>,
        Query.Projector<PrescExampleDTO>,
        SqlTranslator.TableInfo {}
