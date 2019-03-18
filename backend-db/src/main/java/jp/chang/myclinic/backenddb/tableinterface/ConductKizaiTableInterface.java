package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.TableInterface;
import jp.chang.myclinic.dto.ConductKizaiDTO;

public interface ConductKizaiTableInterface extends TableInterface<ConductKizaiDTO> {

  String conductKizaiId();

  String conductId();

  String kizaicode();

  String amount();
}
