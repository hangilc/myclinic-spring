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

public class TextTableBase extends Table<TextDTO> {

  private static List<Column<TextDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "text_id",
                true,
                true,
                (stmt, i, dto) -> stmt.setInt(i, dto.textId),
                (rs, i, dto) -> dto.textId = rs.getObject(i, Integer.class)),
            new Column<>(
                "visit_id",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.visitId),
                (rs, i, dto) -> dto.visitId = rs.getObject(i, Integer.class)),
            new Column<>(
                "content",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.content),
                (rs, i, dto) -> dto.content = rs.getObject(i, String.class)));
  }

  @Override()
  protected String getTableName() {
    return "text";
  }

  @Override()
  protected Class<TextDTO> getClassDTO() {
    return TextDTO.class;
  }

  @Override()
  protected List<Column<TextDTO>> getColumns() {
    return columns;
  }
}
