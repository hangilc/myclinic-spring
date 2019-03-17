package jp.chang.myclinic.backendpgsql.table;

import jp.chang.myclinic.backendpgsql.Query;
import jp.chang.myclinic.backendpgsql.tablebase.TextTableBase;
import jp.chang.myclinic.dto.TextDTO;

import java.util.List;

public class TextTable extends TextTableBase {

    public List<TextDTO> listText(int visitId) {
        String sql = "select * from text where visit_id = ?";
        return Query.query(sql, this, visitId);
    }

}
