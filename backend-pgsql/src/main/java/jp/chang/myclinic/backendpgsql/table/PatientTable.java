package jp.chang.myclinic.backendpgsql.table;

import jp.chang.myclinic.backendpgsql.Query;
import jp.chang.myclinic.backendpgsql.tablebase.PatientTableBase;
import jp.chang.myclinic.dto.PatientDTO;

import java.util.List;

public class PatientTable extends PatientTableBase {

    public List<PatientDTO> searchPatient(String textLastName, String textFirstName) {
        String sql = "select * from patient p where " +
                " (p.last_name like ? or p.last_name_yomi like ?) and " +
                " (p.first_name like ? or p.first_name_yomi like ?)";
        String last = "%" + textLastName + "%";
        String first = "%" + textFirstName + "%";
        return Query.query(sql, this, last, last, first, first);
    }

    public List<PatientDTO> listRecentlyRegisteredPatient(int n){
        String sql = "select * from patient order by patient_id desc limit ?";
        return Query.query(sql, this, n);
    }

}
