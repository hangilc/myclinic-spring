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

public class TextTableBase extends Table<TextDTO> {

  private static List<Column<TextDTO>> columns;

  static {
    columns =
        List.of(
            new Column<TextDTO>(
                "text_id",
                true,
                true,
                (rs, dto) -> dto.textId = rs.getObject("text_id", Integer.class),
                dto -> dto.textId),
            new Column<TextDTO>(
                "visit_id",
                false,
                false,
                (rs, dto) -> dto.visitId = rs.getObject("visit_id", Integer.class),
                dto -> dto.visitId),
            new Column<TextDTO>(
                "content",
                false,
                false,
                (rs, dto) -> dto.content = rs.getObject("content", String.class),
                dto -> dto.content));
  }

  @Override
  protected String getTableName() {
    return "text";
  }

  @Override
  protected Class<TextDTO> getClassDTO() {
    return TextDTO.class;
  }

  @Override
  protected List<Column<TextDTO>> getColumns() {
    return columns;
  }
}
