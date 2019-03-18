package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.TableInterface;
import jp.chang.myclinic.dto.IntraclinicTagDTO;

public interface IntraclinicTagTableInterface extends TableInterface<IntraclinicTagDTO> {

  String tagId();

  String name();
}
