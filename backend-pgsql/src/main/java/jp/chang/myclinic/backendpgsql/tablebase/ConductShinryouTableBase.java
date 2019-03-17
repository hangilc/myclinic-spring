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

public class ConductShinryouTableBase extends Table<ConductShinryouDTO> {

  private static List<Column<ConductShinryouDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "conduct_shinryou_id",
                true,
                true,
                (stmt, i, dto) -> stmt.setInt(i, dto.conductShinryouId),
                (rs, i, dto) -> dto.conductShinryouId = rs.getObject(i, Integer.class)),
            new Column<>(
                "conduct_id",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.conductId),
                (rs, i, dto) -> dto.conductId = rs.getObject(i, Integer.class)),
            new Column<>(
                "shinryoucode",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.shinryoucode),
                (rs, i, dto) -> dto.shinryoucode = rs.getObject(i, Integer.class)));
  }

  @Override()
  protected String getTableName() {
    return "conduct_shinryou";
  }

  @Override()
  protected Class<ConductShinryouDTO> getClassDTO() {
    return ConductShinryouDTO.class;
  }

  @Override()
  protected List<Column<ConductShinryouDTO>> getColumns() {
    return columns;
  }
}
