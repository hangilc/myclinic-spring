package jp.chang.myclinic.backendsqlite.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import jp.chang.myclinic.backenddb.tableinterface.ShinryouTableInterface;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.ShinryouDTO;

public class ShinryouTableBase extends Table<ShinryouDTO> implements ShinryouTableInterface {

  private static List<Column<ShinryouDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "shinryou_id",
                "shinryouId",
                true,
                true,
                (stmt, i, dto) -> stmt.setInt(i, dto.shinryouId),
                (rs, i, dto) -> dto.shinryouId = rs.getObject(i, Integer.class)),
            new Column<>(
                "visit_id",
                "visitId",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.visitId),
                (rs, i, dto) -> dto.visitId = rs.getObject(i, Integer.class)),
            new Column<>(
                "shinryoucode",
                "shinryoucode",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.shinryoucode),
                (rs, i, dto) -> dto.shinryoucode = rs.getObject(i, Integer.class)));
  }

  @Override()
  public String getTableName() {
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
