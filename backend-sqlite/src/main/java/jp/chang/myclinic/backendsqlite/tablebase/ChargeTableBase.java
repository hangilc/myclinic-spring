package jp.chang.myclinic.backendsqlite.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.Query;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.ChargeDTO;

public class ChargeTableBase extends Table<ChargeDTO> {

  public ChargeTableBase(Query query) {
    super(query);
  }

  private static List<Column<ChargeDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "visit_id",
                "visitId",
                true,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.visitId),
                (rs, i, dto) -> dto.visitId = rs.getInt(i)),
            new Column<>(
                "charge",
                "charge",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.charge),
                (rs, i, dto) -> dto.charge = rs.getInt(i)));
  }

  @Override()
  public String getTableName() {
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
