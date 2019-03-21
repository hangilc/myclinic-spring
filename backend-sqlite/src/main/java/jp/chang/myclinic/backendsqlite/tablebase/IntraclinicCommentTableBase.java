package jp.chang.myclinic.backendsqlite.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.Query;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import jp.chang.myclinic.backenddb.tableinterface.IntraclinicCommentTableInterface;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.IntraclinicCommentDTO;

public class IntraclinicCommentTableBase extends Table<IntraclinicCommentDTO>
    implements IntraclinicCommentTableInterface {

  public IntraclinicCommentTableBase(Query query) {
    super(query);
  }

  private static List<Column<IntraclinicCommentDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "id",
                "id",
                true,
                true,
                (stmt, i, dto) -> stmt.setInt(i, dto.id),
                (rs, i, dto) -> dto.id = rs.getObject(i, Integer.class)),
            new Column<>(
                "name",
                "name",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.name),
                (rs, i, dto) -> dto.name = rs.getObject(i, String.class)),
            new Column<>(
                "content",
                "content",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.content),
                (rs, i, dto) -> dto.content = rs.getObject(i, String.class)),
            new Column<>(
                "post_id",
                "postId",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.postId),
                (rs, i, dto) -> dto.postId = rs.getObject(i, Integer.class)),
            new Column<>(
                "created_at",
                "createdAt",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.createdAt),
                (rs, i, dto) -> dto.createdAt = rs.getObject(i, String.class)));
  }

  @Override()
  public String getTableName() {
    return "intraclinic_comment";
  }

  @Override()
  protected Class<IntraclinicCommentDTO> getClassDTO() {
    return IntraclinicCommentDTO.class;
  }

  @Override()
  protected List<Column<IntraclinicCommentDTO>> getColumns() {
    return columns;
  }
}
