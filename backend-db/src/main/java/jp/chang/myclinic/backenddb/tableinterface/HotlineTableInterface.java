package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.*;
import jp.chang.myclinic.dto.HotlineDTO;

public interface HotlineTableInterface
    extends TableInterface<HotlineDTO>, Query.Projector<HotlineDTO>, SqlTranslator.TableInfo {}
