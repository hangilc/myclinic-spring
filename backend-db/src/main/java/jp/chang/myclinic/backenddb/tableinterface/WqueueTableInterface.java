package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.*;
import jp.chang.myclinic.dto.WqueueDTO;

public interface WqueueTableInterface
    extends TableInterface<WqueueDTO>, Query.Projector<WqueueDTO>, SqlTranslator.TableInfo {}
