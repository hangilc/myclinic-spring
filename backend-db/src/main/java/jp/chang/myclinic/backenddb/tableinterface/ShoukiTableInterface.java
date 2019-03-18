package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.TableInterface;
import jp.chang.myclinic.dto.ShoukiDTO;

public interface ShoukiTableInterface extends TableInterface<ShoukiDTO> {

  String visitId();

  String shouki();
}
