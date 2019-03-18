package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.TableInterface;
import jp.chang.myclinic.dto.PatientDTO;

public interface PatientTableInterface extends TableInterface<PatientDTO> {

  String patientId();

  String lastName();

  String firstName();

  String lastNameYomi();

  String firstNameYomi();

  String sex();

  String birthday();

  String address();

  String phone();
}
