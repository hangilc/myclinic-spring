package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.TableInterface;
import jp.chang.myclinic.dto.ShuushokugoMasterDTO;

public interface ShuushokugoMasterTableInterface extends TableInterface<ShuushokugoMasterDTO> {

  String shuushokugocode();

  String name();
}
