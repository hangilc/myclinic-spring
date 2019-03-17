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

public class IntraclinicTagTableBase extends Table<IntraclinicTagDTO> {

  private static List<Column<IntraclinicTagDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "tag_id",
                true,
                true,
                (stmt, i, dto) -> stmt.setInt(i, dto.tagId),
                (rs, i, dto) -> dto.tagId = rs.getObject(i, Integer.class)),
            new Column<>(
                "name",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.name),
                (rs, i, dto) -> dto.name = rs.getObject(i, String.class)));
  }

  @Override()
  protected String getTableName() {
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
