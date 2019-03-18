package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.TableInterface;
import jp.chang.myclinic.dto.ByoumeiMasterDTO;

public interface ByoumeiMasterTableInterface extends TableInterface<ByoumeiMasterDTO> {

  String shoubyoumeicode();

  String name();

  String validFrom();

  String validUpto();
}
