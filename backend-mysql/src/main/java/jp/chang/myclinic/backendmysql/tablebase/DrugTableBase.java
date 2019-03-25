package jp.chang.myclinic.backendmysql.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.Query;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.DrugDTO;

public class DrugTableBase extends Table<DrugDTO> {

  public DrugTableBase(Query query) {
    super(query);
  }

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
                (rs, i, dto) -> dto.drugId = rs.getInt(i)),
            new Column<>(
                "visit_id",
                "visitId",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.visitId),
                (rs, i, dto) -> dto.visitId = rs.getInt(i)),
            new Column<>(
                "d_iyakuhincode",
                "iyakuhincode",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.iyakuhincode),
                (rs, i, dto) -> dto.iyakuhincode = rs.getInt(i)),
            new Column<>(
                "d_amount",
                "amount",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, String.valueOf(dto.amount)),
                (rs, i, dto) -> dto.amount = Double.parseDouble(rs.getString(i))),
            new Column<>(
                "d_usage",
                "usage",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.usage),
                (rs, i, dto) -> dto.usage = rs.getString(i)),
            new Column<>(
                "d_days",
                "days",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.days),
                (rs, i, dto) -> dto.days = rs.getInt(i)),
            new Column<>(
                "d_category",
                "category",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.category),
                (rs, i, dto) -> dto.category = rs.getInt(i)),
            new Column<>(
                "d_prescribed",
                "prescribed",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.prescribed),
                (rs, i, dto) -> dto.prescribed = rs.getInt(i)));
  }

  @Override()
  public String getTableName() {
    return "visit_drug";
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
