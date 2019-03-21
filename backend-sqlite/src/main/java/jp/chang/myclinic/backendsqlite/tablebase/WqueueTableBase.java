package jp.chang.myclinic.backendsqlite.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.Query;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import jp.chang.myclinic.backenddb.tableinterface.WqueueTableInterface;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.WqueueDTO;

public class WqueueTableBase extends Table<WqueueDTO> implements WqueueTableInterface {

  public WqueueTableBase(Query query) {
    super(query);
  }

  private static List<Column<WqueueDTO>> columns;

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
                "wait_state",
                "waitState",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.waitState),
                (rs, i, dto) -> dto.waitState = rs.getObject(i, Integer.class)));
  }

  @Override()
  public String getTableName() {
    return "wqueue";
  }

  @Override()
  protected Class<WqueueDTO> getClassDTO() {
    return WqueueDTO.class;
  }

  @Override()
  protected List<Column<WqueueDTO>> getColumns() {
    return columns;
  }
}
