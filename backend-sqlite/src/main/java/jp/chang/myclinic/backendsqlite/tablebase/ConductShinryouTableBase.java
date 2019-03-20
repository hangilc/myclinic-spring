package jp.chang.myclinic.backendsqlite.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import jp.chang.myclinic.backenddb.tableinterface.ConductShinryouTableInterface;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.ConductShinryouDTO;

public class ConductShinryouTableBase extends Table<ConductShinryouDTO>
    implements ConductShinryouTableInterface {

  private static List<Column<ConductShinryouDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "conduct_shinryou_id",
                "conductShinryouId",
                true,
                true,
                (stmt, i, dto) -> stmt.setInt(i, dto.conductShinryouId),
                (rs, i, dto) -> dto.conductShinryouId = rs.getObject(i, Integer.class)),
            new Column<>(
                "conduct_id",
                "conductId",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.conductId),
                (rs, i, dto) -> dto.conductId = rs.getObject(i, Integer.class)),
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
    return "conduct_shinryou";
  }

  @Override()
  protected Class<ConductShinryouDTO> getClassDTO() {
    return ConductShinryouDTO.class;
  }

  @Override()
  protected List<Column<ConductShinryouDTO>> getColumns() {
    return columns;
  }
}
