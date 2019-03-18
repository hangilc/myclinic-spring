package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.TableInterface;
import jp.chang.myclinic.dto.DrugDTO;

public interface DrugTableInterface extends TableInterface<DrugDTO> {

  String drugId();

  String visitId();

  String iyakuhincode();

  String amount();

  String usage();

  String days();

  String category();

  String prescribed();
}
