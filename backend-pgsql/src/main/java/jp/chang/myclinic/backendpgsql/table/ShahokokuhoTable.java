package jp.chang.myclinic.backendpgsql.table;

import jp.chang.myclinic.backendpgsql.Query;
import jp.chang.myclinic.backendpgsql.tablebase.ShahokokuhoTableBase;
import jp.chang.myclinic.dto.ShahokokuhoDTO;

import java.time.LocalDate;
import java.util.List;

public class ShahokokuhoTable extends ShahokokuhoTableBase {

    public List<ShahokokuhoDTO> findAvailable(int patientId, LocalDate at){
        String sql = "select * from shahokokuho h where h.patient_id = ? and " +
                " h.valid_from <= date(?) and  (h.valid_upto is null or h.valid_upto >= date(?)) ";
        return Query.query(sql, this, patientId, at, at);
    }

}
