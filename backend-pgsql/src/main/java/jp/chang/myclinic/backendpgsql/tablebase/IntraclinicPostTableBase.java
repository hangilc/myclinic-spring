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

public class IntraclinicPostTableBase extends Table<IntraclinicPostDTO> {

  private static List<Column<IntraclinicPostDTO>> columns;

  static {
    columns =
        List.of(
            new Column<IntraclinicPostDTO>(
                "post_id",
                true,
                true,
                (rs, dto) -> dto.id = rs.getObject("post_id", Integer.class),
                dto -> dto.id),
            new Column<IntraclinicPostDTO>(
                "content",
                false,
                false,
                (rs, dto) -> dto.content = rs.getObject("content", String.class),
                dto -> dto.content),
            new Column<IntraclinicPostDTO>(
                "created_at",
                false,
                false,
                (rs, dto) -> dto.createdAt = rs.getObject("created_at", LocalDate.class).toString(),
                dto -> dto.createdAt));
  }

  @Override
  protected String getTableName() {
    return "intraclinic_post";
  }

  @Override
  protected Class<IntraclinicPostDTO> getClassDTO() {
    return IntraclinicPostDTO.class;
  }

  @Override
  protected List<Column<IntraclinicPostDTO>> getColumns() {
    return columns;
  }
}
