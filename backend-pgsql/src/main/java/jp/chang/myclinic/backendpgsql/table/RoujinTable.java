package jp.chang.myclinic.backendpgsql.table;

import jp.chang.myclinic.backendpgsql.Query;
import jp.chang.myclinic.backendpgsql.tablebase.RoujinTableBase;
import jp.chang.myclinic.dto.KoukikoureiDTO;
import jp.chang.myclinic.dto.RoujinDTO;

import java.time.LocalDate;
import java.util.List;

public class RoujinTable extends RoujinTableBase {

    public List<RoujinDTO> findAvailable(int patientId, LocalDate at){
        String sql = "select * from roujin h where h.patient_id = ? and " +
                " h.valid_from <= date(?) and  (h.valid_upto is null or h.valid_upto >= date(?)) ";
        return Query.query(sql, this, patientId, at, at);
    }

}
