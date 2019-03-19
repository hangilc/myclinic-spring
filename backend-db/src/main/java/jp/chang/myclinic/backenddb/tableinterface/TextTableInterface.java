package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.*;
import jp.chang.myclinic.dto.TextDTO;

public interface TextTableInterface
    extends TableInterface<TextDTO>, Query.Projector<TextDTO>, SqlTranslator.TableInfo {}
