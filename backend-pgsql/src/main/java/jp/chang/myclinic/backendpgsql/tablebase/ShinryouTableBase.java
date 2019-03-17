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

public class ShinryouTableBase extends Table<ShinryouDTO> {

  private static List<Column<ShinryouDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "shinryou_id",
                true,
                true,
                (stmt, i, dto) -> stmt.setInt(i, dto.shinryouId),
                (rs, i, dto) -> dto.shinryouId = rs.getObject(i, Integer.class)),
            new Column<>(
                "visit_id",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.visitId),
                (rs, i, dto) -> dto.visitId = rs.getObject(i, Integer.class)),
            new Column<>(
                "shinryoucode",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.shinryoucode),
                (rs, i, dto) -> dto.shinryoucode = rs.getObject(i, Integer.class)));
  }

  @Override()
  protected String getTableName() {
    return "shinryou";
  }

  @Override()
  protected Class<ShinryouDTO> getClassDTO() {
    return ShinryouDTO.class;
  }

  @Override()
  protected List<Column<ShinryouDTO>> getColumns() {
    return columns;
  }
}
