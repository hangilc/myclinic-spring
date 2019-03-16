package jp.chang.myclinic.backendpgsql.table;

import jp.chang.myclinic.backendpgsql.PatientTableBase;
import jp.chang.myclinic.backendpgsql.Query;
import jp.chang.myclinic.dto.PatientDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class PatientTable extends PatientTableBase {

    public List<PatientDTO> searchPatient(String textLastName, String textFirstName) {
        String sql = "select * from patient p where " +
                " (p.last_name like ? or " +
                "  p.last_name_yomi like ?) and " +
                " (p.first_name like ? or " +
                "  p.first_name_yomi like ?)";
        String last = "%" + textLastName + "%";
        String first = "%" + textFirstName + "%";
        return Query.query(getConnection(), sql, this, last, last, first, first);
    }

    public List<PatientDTO> listRecentlyRegisteredPatient(int n){
        String sql = "select * from patient order by patient_id desc limit ?";
        return Query.query(getConnection(), sql, this, n);
    }

}
