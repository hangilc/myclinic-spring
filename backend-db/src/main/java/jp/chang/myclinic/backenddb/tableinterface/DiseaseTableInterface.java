package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.TableInterface;
import jp.chang.myclinic.dto.DiseaseDTO;

public interface DiseaseTableInterface extends TableInterface<DiseaseDTO> {

  String diseaseId();

  String patientId();

  String shoubyoumeicode();

  String startDate();

  String endDate();

  String endReason();
}
