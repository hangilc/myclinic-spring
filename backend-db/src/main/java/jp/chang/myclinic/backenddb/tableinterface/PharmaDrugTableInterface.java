package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.TableInterface;
import jp.chang.myclinic.dto.PharmaDrugDTO;

public interface PharmaDrugTableInterface extends TableInterface<PharmaDrugDTO> {

  String iyakuhincode();

  String description();

  String sideeffect();
}
