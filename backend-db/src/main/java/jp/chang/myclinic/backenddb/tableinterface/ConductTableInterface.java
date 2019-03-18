package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.TableInterface;
import jp.chang.myclinic.dto.ConductDTO;

public interface ConductTableInterface extends TableInterface<ConductDTO> {

  String conductId();

  String visitId();

  String kind();
}
