package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.*;
import jp.chang.myclinic.dto.DrugAttrDTO;

public interface DrugAttrTableInterface
    extends TableInterface<DrugAttrDTO>, Query.Projector<DrugAttrDTO>, SqlTranslator.TableInfo {}
