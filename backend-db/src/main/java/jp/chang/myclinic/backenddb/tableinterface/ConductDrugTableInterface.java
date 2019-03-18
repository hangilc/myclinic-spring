package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.TableInterface;
import jp.chang.myclinic.dto.ConductDrugDTO;

public interface ConductDrugTableInterface extends TableInterface<ConductDrugDTO> {

  String conductDrugId();

  String conductId();

  String iyakuhincode();

  String amount();
}
