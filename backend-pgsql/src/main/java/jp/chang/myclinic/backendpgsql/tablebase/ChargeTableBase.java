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

public class ChargeTableBase extends Table<ChargeDTO> {

  private static List<Column<ChargeDTO>> columns;

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
                "charge",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.charge),
                (rs, i, dto) -> dto.charge = rs.getObject(i, Integer.class)));
  }

  @Override()
  protected String getTableName() {
    return "charge";
  }

  @Override()
  protected Class<ChargeDTO> getClassDTO() {
    return ChargeDTO.class;
  }

  @Override()
  protected List<Column<ChargeDTO>> getColumns() {
    return columns;
  }
}
