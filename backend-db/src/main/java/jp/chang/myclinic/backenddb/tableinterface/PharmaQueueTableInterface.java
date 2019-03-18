package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.TableInterface;
import jp.chang.myclinic.dto.PharmaQueueDTO;

public interface PharmaQueueTableInterface extends TableInterface<PharmaQueueDTO> {

  String visitId();

  String pharmaState();
}
