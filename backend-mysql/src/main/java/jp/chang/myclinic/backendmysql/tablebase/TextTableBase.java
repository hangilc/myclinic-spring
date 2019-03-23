package jp.chang.myclinic.backendmysql.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.Query;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import jp.chang.myclinic.backenddb.tableinterface.TextTableInterface;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.TextDTO;

public class TextTableBase extends Table<TextDTO> implements TextTableInterface {

  public TextTableBase(Query query) {
    super(query);
  }

  private static List<Column<TextDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "text_id",
                "textId",
                true,
                true,
                (stmt, i, dto) -> stmt.setInt(i, dto.textId),
                (rs, i, dto) -> dto.textId = rs.getInt(i)),
            new Column<>(
                "visit_id",
                "visitId",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.visitId),
                (rs, i, dto) -> dto.visitId = rs.getInt(i)),
            new Column<>(
                "content",
                "content",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.content),
                (rs, i, dto) -> dto.content = rs.getString(i)));
  }

  @Override()
  public String getTableName() {
    return "visit_text";
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
