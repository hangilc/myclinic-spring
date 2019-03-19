package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.*;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;

public interface PracticeLogTableInterface
    extends TableInterface<PracticeLogDTO>,
        Query.Projector<PracticeLogDTO>,
        SqlTranslator.TableInfo {}
