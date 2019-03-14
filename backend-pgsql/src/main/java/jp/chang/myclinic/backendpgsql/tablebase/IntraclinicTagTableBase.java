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

public class IntraclinicTagTableBase extends Table<IntraclinicTagDTO> {

  private static List<Column<IntraclinicTagDTO>> columns;

  static {
    columns =
        List.of(
            new Column<IntraclinicTagDTO>(
                "tag_id",
                true,
                true,
                (rs, dto) -> dto.tagId = rs.getObject("tag_id", Integer.class),
                dto -> dto.tagId),
            new Column<IntraclinicTagDTO>(
                "name",
                false,
                false,
                (rs, dto) -> dto.name = rs.getObject("name", String.class),
                dto -> dto.name));
  }

  @Override
  protected String getTableName() {
    return "intraclinic_tag";
  }

  @Override
  protected Class<IntraclinicTagDTO> getClassDTO() {
    return IntraclinicTagDTO.class;
  }

  @Override
  protected List<Column<IntraclinicTagDTO>> getColumns() {
    return columns;
  }
}
