package jp.chang.myclinic.backendsqlite.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.Query;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import jp.chang.myclinic.backenddb.tableinterface.ConductTableInterface;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.ConductDTO;

public class ConductTableBase extends Table<ConductDTO> implements ConductTableInterface {

  public ConductTableBase(Query query) {
    super(query);
  }

  private static List<Column<ConductDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "conduct_id",
                "conductId",
                true,
                true,
                (stmt, i, dto) -> stmt.setInt(i, dto.conductId),
                (rs, i, dto) -> dto.conductId = rs.getObject(i, Integer.class)),
            new Column<>(
                "visit_id",
                "visitId",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.visitId),
                (rs, i, dto) -> dto.visitId = rs.getObject(i, Integer.class)),
            new Column<>(
                "kind",
                "kind",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.kind),
                (rs, i, dto) -> dto.kind = rs.getObject(i, Integer.class)));
  }

  @Override()
  public String getTableName() {
    return "conduct";
  }

  @Override()
  protected Class<ConductDTO> getClassDTO() {
    return ConductDTO.class;
  }

  @Override()
  protected List<Column<ConductDTO>> getColumns() {
    return columns;
  }
}
