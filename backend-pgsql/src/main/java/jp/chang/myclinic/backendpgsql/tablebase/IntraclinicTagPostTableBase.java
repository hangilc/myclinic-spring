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

public class IntraclinicTagPostTableBase extends Table<IntraclinicTagPostDTO> {

  private static List<Column<IntraclinicTagPostDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "tag_id",
                true,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.tagId),
                (rs, i, dto) -> dto.tagId = rs.getObject(i, Integer.class)),
            new Column<>(
                "post_id",
                true,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.postId),
                (rs, i, dto) -> dto.postId = rs.getObject(i, Integer.class)));
  }

  @Override()
  protected String getTableName() {
    return "intraclinic_tag_post";
  }

  @Override()
  protected Class<IntraclinicTagPostDTO> getClassDTO() {
    return IntraclinicTagPostDTO.class;
  }

  @Override()
  protected List<Column<IntraclinicTagPostDTO>> getColumns() {
    return columns;
  }
}
