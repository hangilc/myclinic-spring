package jp.chang.myclinic.backendmysql.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.Query;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.IntraclinicPostDTO;

public class IntraclinicPostTableBase extends Table<IntraclinicPostDTO> {

  public IntraclinicPostTableBase(Query query) {
    super(query);
  }

  private static List<Column<IntraclinicPostDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "id",
                "id",
                true,
                true,
                (stmt, i, dto) -> stmt.setInt(i, dto.id),
                (rs, i, dto) -> dto.id = rs.getInt(i)),
            new Column<>(
                "content",
                "content",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.content),
                (rs, i, dto) -> dto.content = rs.getString(i)),
            new Column<>(
                "created_at",
                "createdAt",
                false,
                false,
                (stmt, i, dto) -> stmt.setObject(i, LocalDate.parse(dto.createdAt)),
                (rs, i, dto) -> dto.createdAt = rs.getObject(i, LocalDate.class).toString()));
  }

  @Override()
  public String getTableName() {
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
