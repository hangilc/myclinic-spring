package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.TableInterface;
import jp.chang.myclinic.dto.ChargeDTO;

public interface ChargeTableInterface extends TableInterface<ChargeDTO> {

  String visitId();

  String charge();
}
