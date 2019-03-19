package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.*;
import jp.chang.myclinic.dto.DiseaseDTO;

public interface DiseaseTableInterface
    extends TableInterface<DiseaseDTO>, Query.Projector<DiseaseDTO>, SqlTranslator.TableInfo {}
