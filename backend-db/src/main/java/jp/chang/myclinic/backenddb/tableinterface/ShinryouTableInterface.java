package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.*;
import jp.chang.myclinic.dto.ShinryouDTO;

public interface ShinryouTableInterface
    extends TableInterface<ShinryouDTO>, Query.Projector<ShinryouDTO>, SqlTranslator.TableInfo {}
