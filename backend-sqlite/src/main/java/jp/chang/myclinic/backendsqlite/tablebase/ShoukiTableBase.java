package jp.chang.myclinic.backendsqlite.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.Query;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.ShoukiDTO;

public class ShoukiTableBase extends Table<ShoukiDTO> {

  public ShoukiTableBase(Query query) {
    super(query);
  }

  private static List<Column<ShoukiDTO>> columns;

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
                "shouki",
                "shouki",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.shouki),
                (rs, i, dto) -> dto.shouki = rs.getString(i)));
  }

  @Override()
  public String getTableName() {
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
