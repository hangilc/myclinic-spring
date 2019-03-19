package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.*;
import jp.chang.myclinic.dto.DiseaseAdjDTO;

public interface DiseaseAdjTableInterface
    extends TableInterface<DiseaseAdjDTO>,
        Query.Projector<DiseaseAdjDTO>,
        SqlTranslator.TableInfo {}
