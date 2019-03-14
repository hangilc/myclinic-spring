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

public class ConductShinryouTableBase extends Table<ConductShinryouDTO> {

  private static List<Column<ConductShinryouDTO>> columns;

  static {
    columns =
        List.of(
            new Column<ConductShinryouDTO>(
                "conduct_shinryou_id",
                true,
                true,
                (rs, dto) ->
                    dto.conductShinryouId = rs.getObject("conduct_shinryou_id", Integer.class),
                dto -> dto.conductShinryouId),
            new Column<ConductShinryouDTO>(
                "conduct_id",
                false,
                false,
                (rs, dto) -> dto.conductId = rs.getObject("conduct_id", Integer.class),
                dto -> dto.conductId),
            new Column<ConductShinryouDTO>(
                "shinryoucode",
                false,
                false,
                (rs, dto) -> dto.shinryoucode = rs.getObject("shinryoucode", Integer.class),
                dto -> dto.shinryoucode));
  }

  @Override
  protected String getTableName() {
    return "conduct_shinryou";
  }

  @Override
  protected Class<ConductShinryouDTO> getClassDTO() {
    return ConductShinryouDTO.class;
  }

  @Override
  protected List<Column<ConductShinryouDTO>> getColumns() {
    return columns;
  }
}
