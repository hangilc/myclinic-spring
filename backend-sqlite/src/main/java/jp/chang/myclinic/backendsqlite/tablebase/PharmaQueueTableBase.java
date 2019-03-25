package jp.chang.myclinic.backendsqlite.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.Query;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.PharmaQueueDTO;

public class PharmaQueueTableBase extends Table<PharmaQueueDTO> {

  public PharmaQueueTableBase(Query query) {
    super(query);
  }

  private static List<Column<PharmaQueueDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "visit_id",
                "visitId",
                true,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.visitId),
                (rs, i, dto) -> dto.visitId = rs.getInt(i)),
            new Column<>(
                "pharma_state",
                "pharmaState",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.pharmaState),
                (rs, i, dto) -> dto.pharmaState = rs.getInt(i)));
  }

  @Override()
  public String getTableName() {
    return "pharma_queue";
  }

  @Override()
  protected Class<PharmaQueueDTO> getClassDTO() {
    return PharmaQueueDTO.class;
  }

  @Override()
  protected List<Column<PharmaQueueDTO>> getColumns() {
    return columns;
  }
}
