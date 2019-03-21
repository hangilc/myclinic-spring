package jp.chang.myclinic.backendsqlite.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.Query;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import jp.chang.myclinic.backenddb.tableinterface.IntraclinicTagPostTableInterface;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.IntraclinicTagPostDTO;

public class IntraclinicTagPostTableBase extends Table<IntraclinicTagPostDTO>
    implements IntraclinicTagPostTableInterface {

  public IntraclinicTagPostTableBase(Query query) {
    super(query);
  }

  private static List<Column<IntraclinicTagPostDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "tag_id",
                "tagId",
                true,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.tagId),
                (rs, i, dto) -> dto.tagId = rs.getInt(i)),
            new Column<>(
                "post_id",
                "postId",
                true,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.postId),
                (rs, i, dto) -> dto.postId = rs.getInt(i)));
  }

  @Override()
  public String getTableName() {
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
