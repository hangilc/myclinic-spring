package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.*;
import jp.chang.myclinic.dto.DrugDTO;

public interface DrugTableInterface
    extends TableInterface<DrugDTO>, Query.Projector<DrugDTO>, SqlTranslator.TableInfo {}
