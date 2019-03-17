package jp.chang.myclinic.backendpgsql.tablebase;

import jp.chang.myclinic.backendpgsql.Column;
import jp.chang.myclinic.backendpgsql.Table;
import jp.chang.myclinic.backendpgsql.tablebasehelper.TableBaseHelper;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.logdto.practicelog.*;
import java.sql.Connection;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;

public class PharmaQueueTableBase extends Table<PharmaQueueDTO> {

  private static List<Column<PharmaQueueDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "visit_id",
                true,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.visitId),
                (rs, i, dto) -> dto.visitId = rs.getObject(i, Integer.class)),
            new Column<>(
                "pharma_state",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.pharmaState),
                (rs, i, dto) -> dto.pharmaState = rs.getObject(i, Integer.class)));
  }

  @Override()
  protected String getTableName() {
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
