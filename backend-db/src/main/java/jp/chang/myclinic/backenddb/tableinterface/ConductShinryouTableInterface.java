package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.TableInterface;
import jp.chang.myclinic.dto.ConductShinryouDTO;

public interface ConductShinryouTableInterface extends TableInterface<ConductShinryouDTO> {

  String conductShinryouId();

  String conductId();

  String shinryoucode();
}
