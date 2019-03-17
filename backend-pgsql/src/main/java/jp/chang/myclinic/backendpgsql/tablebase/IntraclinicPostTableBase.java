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

public class IntraclinicPostTableBase extends Table<IntraclinicPostDTO> {

  private static List<Column<IntraclinicPostDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "post_id",
                true,
                true,
                (stmt, i, dto) -> stmt.setInt(i, dto.id),
                (rs, i, dto) -> dto.id = rs.getObject(i, Integer.class)),
            new Column<>(
                "content",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.content),
                (rs, i, dto) -> dto.content = rs.getObject(i, String.class)),
            new Column<>(
                "created_at",
                false,
                false,
                (stmt, i, dto) -> stmt.setObject(i, LocalDate.parse(dto.createdAt)),
                (rs, i, dto) -> dto.createdAt = rs.getObject(i, LocalDate.class).toString()));
  }

  @Override()
  protected String getTableName() {
    return "intraclinic_post";
  }

  @Override()
  protected Class<IntraclinicPostDTO> getClassDTO() {
    return IntraclinicPostDTO.class;
  }

  @Override()
  protected List<Column<IntraclinicPostDTO>> getColumns() {
    return columns;
  }
}
