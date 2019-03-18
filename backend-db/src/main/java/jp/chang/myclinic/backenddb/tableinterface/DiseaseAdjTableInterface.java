package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.TableInterface;
import jp.chang.myclinic.dto.DiseaseAdjDTO;

public interface DiseaseAdjTableInterface extends TableInterface<DiseaseAdjDTO> {

  String diseaseAdjId();

  String diseaseId();

  String shuushokugocode();
}
