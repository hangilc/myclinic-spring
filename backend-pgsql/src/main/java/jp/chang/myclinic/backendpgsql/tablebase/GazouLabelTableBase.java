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

public class GazouLabelTableBase extends Table<GazouLabelDTO> {

  private static List<Column<GazouLabelDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "conduct_id",
                true,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.conductId),
                (rs, i, dto) -> dto.conductId = rs.getObject(i, Integer.class)),
            new Column<>(
                "label",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.label),
                (rs, i, dto) -> dto.label = rs.getObject(i, String.class)));
  }

  @Override()
  protected String getTableName() {
    return "gazou_label";
  }

  @Override()
  protected Class<GazouLabelDTO> getClassDTO() {
    return GazouLabelDTO.class;
  }

  @Override()
  protected List<Column<GazouLabelDTO>> getColumns() {
    return columns;
  }
}
