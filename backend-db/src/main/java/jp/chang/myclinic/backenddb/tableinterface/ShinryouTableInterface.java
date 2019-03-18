package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.TableInterface;
import jp.chang.myclinic.dto.ShinryouDTO;

public interface ShinryouTableInterface extends TableInterface<ShinryouDTO> {

  String shinryouId();

  String visitId();

  String shinryoucode();
}
