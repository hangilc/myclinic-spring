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

public class DrugTableBase extends Table<DrugDTO> {

  private static List<Column<DrugDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "drug_id",
                true,
                true,
                (stmt, i, dto) -> stmt.setInt(i, dto.drugId),
                (rs, i, dto) -> dto.drugId = rs.getObject(i, Integer.class)),
            new Column<>(
                "visit_id",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.visitId),
                (rs, i, dto) -> dto.visitId = rs.getObject(i, Integer.class)),
            new Column<>(
                "iyakuhincode",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.iyakuhincode),
                (rs, i, dto) -> dto.iyakuhincode = rs.getObject(i, Integer.class)),
            new Column<>(
                "amount",
                false,
                false,
                (stmt, i, dto) -> stmt.setBigDecimal(i, new BigDecimal(dto.amount)),
                (rs, i, dto) -> dto.amount = rs.getObject(i, BigDecimal.class).doubleValue()),
            new Column<>(
                "usage",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.usage),
                (rs, i, dto) -> dto.usage = rs.getObject(i, String.class)),
            new Column<>(
                "days",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.days),
                (rs, i, dto) -> dto.days = rs.getObject(i, Integer.class)),
            new Column<>(
                "category",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.category),
                (rs, i, dto) -> dto.category = rs.getObject(i, Integer.class)),
            new Column<>(
                "prescribed",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.prescribed),
                (rs, i, dto) -> dto.prescribed = rs.getObject(i, Integer.class)));
  }

  @Override()
  protected String getTableName() {
    return "drug";
  }

  @Override()
  protected Class<DrugDTO> getClassDTO() {
    return DrugDTO.class;
  }

  @Override()
  protected List<Column<DrugDTO>> getColumns() {
    return columns;
  }
}
