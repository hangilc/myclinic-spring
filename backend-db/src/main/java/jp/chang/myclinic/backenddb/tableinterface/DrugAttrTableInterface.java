package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.TableInterface;
import jp.chang.myclinic.dto.DrugAttrDTO;

public interface DrugAttrTableInterface extends TableInterface<DrugAttrDTO> {

  String drugId();

  String tekiyou();
}
