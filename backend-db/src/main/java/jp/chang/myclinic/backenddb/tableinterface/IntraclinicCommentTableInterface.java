package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.*;
import jp.chang.myclinic.dto.IntraclinicCommentDTO;

public interface IntraclinicCommentTableInterface
    extends TableInterface<IntraclinicCommentDTO>,
        Query.Projector<IntraclinicCommentDTO>,
        SqlTranslator.TableInfo {}
