package jp.chang.myclinic.backendsqlite.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.Query;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import jp.chang.myclinic.backenddb.tableinterface.IntraclinicTagTableInterface;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.IntraclinicTagDTO;

public class IntraclinicTagTableBase extends Table<IntraclinicTagDTO>
    implements IntraclinicTagTableInterface {

  public IntraclinicTagTableBase(Query query) {
    super(query);
  }

  private static List<Column<IntraclinicTagDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "tag_id",
                "tagId",
                true,
                true,
                (stmt, i, dto) -> stmt.setInt(i, dto.tagId),
                (rs, i, dto) -> dto.tagId = rs.getInt(i)),
            new Column<>(
                "name",
                "name",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.name),
                (rs, i, dto) -> dto.name = rs.getString(i)));
  }

  @Override()
  public String getTableName() {
    return "intraclinic_tag";
  }

  @Override()
  protected Class<IntraclinicTagDTO> getClassDTO() {
    return IntraclinicTagDTO.class;
  }

  @Override()
  protected List<Column<IntraclinicTagDTO>> getColumns() {
    return columns;
  }
}
