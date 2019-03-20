package jp.chang.myclinic.backendsqlite.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import jp.chang.myclinic.backenddb.tableinterface.ShoukiTableInterface;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.ShoukiDTO;

public class ShoukiTableBase extends Table<ShoukiDTO> implements ShoukiTableInterface {

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
                (rs, i, dto) -> dto.visitId = rs.getObject(i, Integer.class)),
            new Column<>(
                "shouki",
                "shouki",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.shouki),
                (rs, i, dto) -> dto.shouki = rs.getObject(i, String.class)));
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
