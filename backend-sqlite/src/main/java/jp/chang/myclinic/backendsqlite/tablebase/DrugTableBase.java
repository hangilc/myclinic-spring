package jp.chang.myclinic.backendsqlite.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import jp.chang.myclinic.backenddb.tableinterface.DrugTableInterface;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.DrugDTO;

public class DrugTableBase extends Table<DrugDTO> implements DrugTableInterface {

  private static List<Column<DrugDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "drug_id",
                "drugId",
                true,
                true,
                (stmt, i, dto) -> stmt.setInt(i, dto.drugId),
                (rs, i, dto) -> dto.drugId = rs.getObject(i, Integer.class)),
            new Column<>(
                "visit_id",
                "visitId",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.visitId),
                (rs, i, dto) -> dto.visitId = rs.getObject(i, Integer.class)),
            new Column<>(
                "iyakuhincode",
                "iyakuhincode",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.iyakuhincode),
                (rs, i, dto) -> dto.iyakuhincode = rs.getObject(i, Integer.class)),
            new Column<>(
                "amount",
                "amount",
                false,
                false,
                (stmt, i, dto) -> stmt.setDouble(i, dto.amount),
                (rs, i, dto) -> dto.amount = rs.getObject(i, Double.class)),
            new Column<>(
                "usage",
                "usage",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.usage),
                (rs, i, dto) -> dto.usage = rs.getObject(i, String.class)),
            new Column<>(
                "days",
                "days",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.days),
                (rs, i, dto) -> dto.days = rs.getObject(i, Integer.class)),
            new Column<>(
                "category",
                "category",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.category),
                (rs, i, dto) -> dto.category = rs.getObject(i, Integer.class)),
            new Column<>(
                "prescribed",
                "prescribed",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.prescribed),
                (rs, i, dto) -> dto.prescribed = rs.getObject(i, Integer.class)));
  }

  @Override()
  public String getTableName() {
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
