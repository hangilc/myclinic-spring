package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.*;
import jp.chang.myclinic.dto.VisitDTO;

public interface VisitTableInterface
    extends TableInterface<VisitDTO>, Query.Projector<VisitDTO>, SqlTranslator.TableInfo {}
