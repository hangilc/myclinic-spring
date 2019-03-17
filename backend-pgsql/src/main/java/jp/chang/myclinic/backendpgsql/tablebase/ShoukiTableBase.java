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

public class ShoukiTableBase extends Table<ShoukiDTO> {

  private static List<Column<ShoukiDTO>> columns;

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
                "shouki",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.shouki),
                (rs, i, dto) -> dto.shouki = rs.getObject(i, String.class)));
  }

  @Override()
  protected String getTableName() {
    return "shouki";
  }

  @Override()
  protected Class<ShoukiDTO> getClassDTO() {
    return ShoukiDTO.class;
  }

  @Override()
  protected List<Column<ShoukiDTO>> getColumns() {
    return columns;
  }
}
