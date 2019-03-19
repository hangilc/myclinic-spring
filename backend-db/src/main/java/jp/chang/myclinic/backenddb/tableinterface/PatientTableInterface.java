package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.*;
import jp.chang.myclinic.dto.PatientDTO;

public interface PatientTableInterface
    extends TableInterface<PatientDTO>, Query.Projector<PatientDTO>, SqlTranslator.TableInfo {}
