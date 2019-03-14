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

public class IntraclinicCommentTableBase extends Table<IntraclinicCommentDTO> {

  private static List<Column<IntraclinicCommentDTO>> columns;

  static {
    columns =
        List.of(
            new Column<IntraclinicCommentDTO>(
                "comment_id",
                true,
                true,
                (rs, dto) -> dto.id = rs.getObject("comment_id", Integer.class),
                dto -> dto.id),
            new Column<IntraclinicCommentDTO>(
                "name",
                false,
                false,
                (rs, dto) -> dto.name = rs.getObject("name", String.class),
                dto -> dto.name),
            new Column<IntraclinicCommentDTO>(
                "content",
                false,
                false,
                (rs, dto) -> dto.content = rs.getObject("content", String.class),
                dto -> dto.content),
            new Column<IntraclinicCommentDTO>(
                "post_id",
                false,
                false,
                (rs, dto) -> dto.postId = rs.getObject("post_id", Integer.class),
                dto -> dto.postId),
            new Column<IntraclinicCommentDTO>(
                "created_at",
                false,
                false,
                (rs, dto) -> dto.createdAt = rs.getObject("created_at", LocalDate.class).toString(),
                dto -> dto.createdAt));
  }

  @Override
  protected String getTableName() {
    return "intraclinic_comment";
  }

  @Override
  protected Class<IntraclinicCommentDTO> getClassDTO() {
    return IntraclinicCommentDTO.class;
  }

  @Override
  protected List<Column<IntraclinicCommentDTO>> getColumns() {
    return columns;
  }
}
