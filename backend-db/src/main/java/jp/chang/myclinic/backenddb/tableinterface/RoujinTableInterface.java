package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.*;
import jp.chang.myclinic.dto.RoujinDTO;

public interface RoujinTableInterface
    extends TableInterface<RoujinDTO>, Query.Projector<RoujinDTO>, SqlTranslator.TableInfo {}
