package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.*;
import jp.chang.myclinic.dto.KouhiDTO;

public interface KouhiTableInterface
    extends TableInterface<KouhiDTO>, Query.Projector<KouhiDTO>, SqlTranslator.TableInfo {}
