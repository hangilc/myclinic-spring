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

public class HotlineTableBase extends Table<HotlineDTO> {

  private static List<Column<HotlineDTO>> columns;

  static {
    columns =
        List.of(
            new Column<HotlineDTO>(
                "hotline_id",
                true,
                true,
                (rs, dto) -> dto.hotlineId = rs.getObject("hotline_id", Integer.class),
                dto -> dto.hotlineId),
            new Column<HotlineDTO>(
                "message",
                false,
                false,
                (rs, dto) -> dto.message = rs.getObject("message", String.class),
                dto -> dto.message),
            new Column<HotlineDTO>(
                "sender",
                false,
                false,
                (rs, dto) -> dto.sender = rs.getObject("sender", String.class),
                dto -> dto.sender),
            new Column<HotlineDTO>(
                "recipient",
                false,
                false,
                (rs, dto) -> dto.recipient = rs.getObject("recipient", String.class),
                dto -> dto.recipient),
            new Column<HotlineDTO>(
                "posted_at",
                false,
                false,
                (rs, dto) ->
                    dto.postedAt =
                        TableBaseHelper.localDateTimeToString(
                            rs.getObject("posted_at", LocalDateTime.class)),
                dto -> dto.postedAt));
  }

  @Override
  protected String getTableName() {
    return "hotline";
  }

  @Override
  protected Class<HotlineDTO> getClassDTO() {
    return HotlineDTO.class;
  }

  @Override
  protected List<Column<HotlineDTO>> getColumns() {
    return columns;
  }
}
