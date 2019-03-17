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

public class HotlineTableBase extends Table<HotlineDTO> {

  private static List<Column<HotlineDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "hotline_id",
                true,
                true,
                (stmt, i, dto) -> stmt.setInt(i, dto.hotlineId),
                (rs, i, dto) -> dto.hotlineId = rs.getObject(i, Integer.class)),
            new Column<>(
                "message",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.message),
                (rs, i, dto) -> dto.message = rs.getObject(i, String.class)),
            new Column<>(
                "sender",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.sender),
                (rs, i, dto) -> dto.sender = rs.getObject(i, String.class)),
            new Column<>(
                "recipient",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.recipient),
                (rs, i, dto) -> dto.recipient = rs.getObject(i, String.class)),
            new Column<>(
                "posted_at",
                false,
                false,
                (stmt, i, dto) ->
                    stmt.setObject(i, TableBaseHelper.stringToLocalDateTime(dto.postedAt)),
                (rs, i, dto) ->
                    dto.postedAt =
                        TableBaseHelper.localDateTimeToString(
                            rs.getObject(i, LocalDateTime.class))));
  }

  @Override()
  protected String getTableName() {
    return "hotline";
  }

  @Override()
  protected Class<HotlineDTO> getClassDTO() {
    return HotlineDTO.class;
  }

  @Override()
  protected List<Column<HotlineDTO>> getColumns() {
    return columns;
  }
}
