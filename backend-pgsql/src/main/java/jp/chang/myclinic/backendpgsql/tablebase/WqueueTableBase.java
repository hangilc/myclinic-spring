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

public class WqueueTableBase extends Table<WqueueDTO> {

  private static List<Column<WqueueDTO>> columns;

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
                "wait_state",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.waitState),
                (rs, i, dto) -> dto.waitState = rs.getObject(i, Integer.class)));
  }

  @Override()
  protected String getTableName() {
    return "wqueue";
  }

  @Override()
  protected Class<WqueueDTO> getClassDTO() {
    return WqueueDTO.class;
  }

  @Override()
  protected List<Column<WqueueDTO>> getColumns() {
    return columns;
  }
}
