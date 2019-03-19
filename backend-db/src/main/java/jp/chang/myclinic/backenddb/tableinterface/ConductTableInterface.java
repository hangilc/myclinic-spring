package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.*;
import jp.chang.myclinic.dto.ConductDTO;

public interface ConductTableInterface
    extends TableInterface<ConductDTO>, Query.Projector<ConductDTO>, SqlTranslator.TableInfo {}
