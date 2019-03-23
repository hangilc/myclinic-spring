package jp.chang.myclinic.backendmysql.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.Query;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import jp.chang.myclinic.backenddb.tableinterface.HotlineTableInterface;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.HotlineDTO;

public class HotlineTableBase extends Table<HotlineDTO> implements HotlineTableInterface {

  public HotlineTableBase(Query query) {
    super(query);
  }

  private static List<Column<HotlineDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "hotline_id",
                "hotlineId",
                true,
                true,
                (stmt, i, dto) -> stmt.setInt(i, dto.hotlineId),
                (rs, i, dto) -> dto.hotlineId = rs.getInt(i)),
            new Column<>(
                "message",
                "message",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.message),
                (rs, i, dto) -> dto.message = rs.getString(i)),
            new Column<>(
                "sender",
                "sender",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.sender),
                (rs, i, dto) -> dto.sender = rs.getString(i)),
            new Column<>(
                "recipient",
                "recipient",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.recipient),
                (rs, i, dto) -> dto.recipient = rs.getString(i)),
            new Column<>(
                "m_datetime",
                "postedAt",
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
  public String getTableName() {
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
