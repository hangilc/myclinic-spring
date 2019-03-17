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

public class ConductTableBase extends Table<ConductDTO> {

  private static List<Column<ConductDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "conduct_id",
                true,
                true,
                (stmt, i, dto) -> stmt.setInt(i, dto.conductId),
                (rs, i, dto) -> dto.conductId = rs.getObject(i, Integer.class)),
            new Column<>(
                "visit_id",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.visitId),
                (rs, i, dto) -> dto.visitId = rs.getObject(i, Integer.class)),
            new Column<>(
                "kind",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.kind),
                (rs, i, dto) -> dto.kind = rs.getObject(i, Integer.class)));
  }

  @Override()
  protected String getTableName() {
    return "conduct";
  }

  @Override()
  protected Class<ConductDTO> getClassDTO() {
    return ConductDTO.class;
  }

  @Override()
  protected List<Column<ConductDTO>> getColumns() {
    return columns;
  }
}
