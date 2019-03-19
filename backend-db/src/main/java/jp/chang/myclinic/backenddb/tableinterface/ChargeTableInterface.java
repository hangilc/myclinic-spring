package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.*;
import jp.chang.myclinic.dto.ChargeDTO;

public interface ChargeTableInterface
    extends TableInterface<ChargeDTO>, Query.Projector<ChargeDTO>, SqlTranslator.TableInfo {}
