package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.*;
import jp.chang.myclinic.dto.ShoukiDTO;

public interface ShoukiTableInterface
    extends TableInterface<ShoukiDTO>, Query.Projector<ShoukiDTO>, SqlTranslator.TableInfo {}
