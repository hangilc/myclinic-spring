package jp.chang.myclinic.backendpgsql.tablebase;

import jp.chang.myclinic.backendpgsql.Column;
import jp.chang.myclinic.backendpgsql.Table;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.logdto.practicelog.*;
import java.sql.Connection;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.backendpgsql.tablebasehelper.TableBaseHelper;

public class PracticeLogTableBase extends Table<PracticeLogDTO> {

  private static List<Column<PracticeLogDTO>> columns;

  static {
    columns =
        List.of(
            new Column<PracticeLogDTO>(
                "practice_log_id",
                true,
                true,
                (rs, dto) -> dto.serialId = rs.getObject("practice_log_id", Integer.class),
                dto -> dto.serialId),
            new Column<PracticeLogDTO>(
                "created_at",
                false,
                false,
                (rs, dto) ->
                    dto.createdAt =
                        TableBaseHelper.localDateTimeToString(
                            rs.getObject("created_at", LocalDateTime.class)),
                dto -> dto.createdAt),
            new Column<PracticeLogDTO>(
                "kind",
                false,
                false,
                (rs, dto) -> dto.kind = rs.getObject("kind", String.class),
                dto -> dto.kind),
            new Column<PracticeLogDTO>(
                "body",
                false,
                false,
                (rs, dto) -> dto.body = rs.getObject("body", String.class),
                dto -> dto.body));
  }

  @Override
  protected String getTableName() {
    return "practice_log";
  }

  @Override
  protected Class<PracticeLogDTO> getClassDTO() {
    return PracticeLogDTO.class;
  }

  @Override
  protected List<Column<PracticeLogDTO>> getColumns() {
    return columns;
  }
}
