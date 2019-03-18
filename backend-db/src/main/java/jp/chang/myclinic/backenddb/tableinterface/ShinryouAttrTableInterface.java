package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.TableInterface;
import jp.chang.myclinic.dto.ShinryouAttrDTO;

public interface ShinryouAttrTableInterface extends TableInterface<ShinryouAttrDTO> {

  String shinryouId();

  String tekiyou();
}
