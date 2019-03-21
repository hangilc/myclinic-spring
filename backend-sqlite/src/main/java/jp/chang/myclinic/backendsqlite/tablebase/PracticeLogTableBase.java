package jp.chang.myclinic.backendsqlite.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.Query;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import jp.chang.myclinic.backenddb.tableinterface.PracticeLogTableInterface;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;

public class PracticeLogTableBase extends Table<PracticeLogDTO>
    implements PracticeLogTableInterface {

  public PracticeLogTableBase(Query query) {
    super(query);
  }

  private static List<Column<PracticeLogDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "serial_id",
                "serialId",
                true,
                true,
                (stmt, i, dto) -> stmt.setInt(i, dto.serialId),
                (rs, i, dto) -> dto.serialId = rs.getInt(i)),
            new Column<>(
                "created_at",
                "createdAt",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.createdAt),
                (rs, i, dto) -> dto.createdAt = rs.getString(i)),
            new Column<>(
                "kind",
                "kind",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.kind),
                (rs, i, dto) -> dto.kind = rs.getString(i)),
            new Column<>(
                "body",
                "body",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.body),
                (rs, i, dto) -> dto.body = rs.getString(i)));
  }

  @Override()
  public String getTableName() {
    return "practice_log";
  }

  @Override()
  protected Class<PracticeLogDTO> getClassDTO() {
    return PracticeLogDTO.class;
  }

  @Override()
  protected List<Column<PracticeLogDTO>> getColumns() {
    return columns;
  }
}
