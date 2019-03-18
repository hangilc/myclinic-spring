package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.TableInterface;
import jp.chang.myclinic.dto.GazouLabelDTO;

public interface GazouLabelTableInterface extends TableInterface<GazouLabelDTO> {

  String conductId();

  String label();
}
