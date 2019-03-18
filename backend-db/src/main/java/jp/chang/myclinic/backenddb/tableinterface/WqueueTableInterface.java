package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.TableInterface;
import jp.chang.myclinic.dto.WqueueDTO;

public interface WqueueTableInterface extends TableInterface<WqueueDTO> {

  String visitId();

  String waitState();
}
