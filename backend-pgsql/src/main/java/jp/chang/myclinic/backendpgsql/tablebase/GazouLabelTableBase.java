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

public class GazouLabelTableBase extends Table<GazouLabelDTO> {

  private static List<Column<GazouLabelDTO>> columns;

  static {
    columns =
        List.of(
            new Column<GazouLabelDTO>(
                "conduct_id",
                true,
                false,
                (rs, dto) -> dto.conductId = rs.getObject("conduct_id", Integer.class),
                dto -> dto.conductId),
            new Column<GazouLabelDTO>(
                "label",
                false,
                false,
                (rs, dto) -> dto.label = rs.getObject("label", String.class),
                dto -> dto.label));
  }

  @Override
  protected String getTableName() {
    return "gazou_label";
  }

  @Override
  protected Class<GazouLabelDTO> getClassDTO() {
    return GazouLabelDTO.class;
  }

  @Override
  protected List<Column<GazouLabelDTO>> getColumns() {
    return columns;
  }
}
