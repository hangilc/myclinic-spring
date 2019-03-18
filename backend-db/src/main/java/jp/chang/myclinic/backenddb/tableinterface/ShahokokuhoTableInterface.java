package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.TableInterface;
import jp.chang.myclinic.dto.ShahokokuhoDTO;

public interface ShahokokuhoTableInterface extends TableInterface<ShahokokuhoDTO> {

  String shahokokuhoId();

  String patientId();

  String hokenshaBangou();

  String hihokenshaKigou();

  String hihokenshaBangou();

  String honnin();

  String validFrom();

  String validUpto();

  String kourei();
}
