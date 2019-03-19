package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.*;
import jp.chang.myclinic.dto.KoukikoureiDTO;

public interface KoukikoureiTableInterface
    extends TableInterface<KoukikoureiDTO>,
        Query.Projector<KoukikoureiDTO>,
        SqlTranslator.TableInfo {}
