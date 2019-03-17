package jp.chang.myclinic.backendpgsql.table;

import jp.chang.myclinic.backendpgsql.Query;
import jp.chang.myclinic.backendpgsql.tablebase.KoukikoureiTableBase;
import jp.chang.myclinic.dto.KoukikoureiDTO;

import java.time.LocalDate;
import java.util.List;

public class KoukikoureiTable extends KoukikoureiTableBase {

    public List<KoukikoureiDTO> findAvailable(int patientId, LocalDate at){
        String sql = "select * from koukikourei h where h.patient_id = ? and " +
                " h.valid_from <= date(?) and  (h.valid_upto is null or h.valid_upto >= date(?)) ";
        return Query.query(sql, this, patientId, at, at);
    }

}
