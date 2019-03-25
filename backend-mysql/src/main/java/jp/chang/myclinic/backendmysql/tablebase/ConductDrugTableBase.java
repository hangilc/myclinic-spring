package jp.chang.myclinic.backendmysql.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.Query;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.ConductDrugDTO;

public class ConductDrugTableBase extends Table<ConductDrugDTO> {

  public ConductDrugTableBase(Query query) {
    super(query);
  }

  private static List<Column<ConductDrugDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "id",
                "conductDrugId",
                true,
                true,
                (stmt, i, dto) -> stmt.setInt(i, dto.conductDrugId),
                (rs, i, dto) -> dto.conductDrugId = rs.getInt(i)),
            new Column<>(
                "visit_conduct_id",
                "conductId",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.conductId),
                (rs, i, dto) -> dto.conductId = rs.getInt(i)),
            new Column<>(
                "iyakuhincode",
                "iyakuhincode",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.iyakuhincode),
                (rs, i, dto) -> dto.iyakuhincode = rs.getInt(i)),
            new Column<>(
                "amount",
                "amount",
                false,
                false,
                (stmt, i, dto) -> stmt.setDouble(i, dto.amount),
                (rs, i, dto) -> dto.amount = rs.getDouble(i)));
  }

  @Override()
  public String getTableName() {
    return "visit_conduct_drug";
  }

  @Override()
  protected Class<ConductDrugDTO> getClassDTO() {
    return ConductDrugDTO.class;
  }

  @Override()
  protected List<Column<ConductDrugDTO>> getColumns() {
    return columns;
  }
}
