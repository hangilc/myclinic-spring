package jp.chang.myclinic.backendpgsql.table;

import jp.chang.myclinic.backendpgsql.Query;
import jp.chang.myclinic.backendpgsql.tablebase.KouhiTableBase;
import jp.chang.myclinic.dto.KouhiDTO;

import java.time.LocalDate;
import java.util.List;

public class KouhiTable extends KouhiTableBase {

    public List<KouhiDTO> findAvailable(int patientId, LocalDate at){
        String sql = "select * from kouhi h where h.patient_id = ? and " +
                " h.valid_from <= date(?) and  (h.valid_upto is null or h.valid_upto >= date(?)) ";
        return Query.query(sql, this, patientId, at, at);
    }

}
