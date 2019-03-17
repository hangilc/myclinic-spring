package jp.chang.myclinic.backendpgsql.tablebase;

import jp.chang.myclinic.backendpgsql.Column;
import jp.chang.myclinic.backendpgsql.Table;
import jp.chang.myclinic.backendpgsql.tablebasehelper.TableBaseHelper;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.logdto.practicelog.*;
import java.sql.Connection;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;

public class PracticeLogTableBase extends Table<PracticeLogDTO> {

  private static List<Column<PracticeLogDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "practice_log_id",
                true,
                true,
                (stmt, i, dto) -> stmt.setInt(i, dto.serialId),
                (rs, i, dto) -> dto.serialId = rs.getObject(i, Integer.class)),
            new Column<>(
                "created_at",
                false,
                false,
                (stmt, i, dto) ->
                    stmt.setObject(i, TableBaseHelper.stringToLocalDateTime(dto.createdAt)),
                (rs, i, dto) ->
                    dto.createdAt =
                        TableBaseHelper.localDateTimeToString(
                            rs.getObject(i, LocalDateTime.class))),
            new Column<>(
                "kind",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.kind),
                (rs, i, dto) -> dto.kind = rs.getObject(i, String.class)),
            new Column<>(
                "body",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.body),
                (rs, i, dto) -> dto.body = rs.getObject(i, String.class)));
  }

  @Override()
  protected String getTableName() {
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
