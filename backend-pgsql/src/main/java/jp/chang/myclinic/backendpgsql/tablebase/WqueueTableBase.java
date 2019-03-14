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

public class WqueueTableBase extends Table<WqueueDTO> {

  private static List<Column<WqueueDTO>> columns;

  static {
    columns =
        List.of(
            new Column<WqueueDTO>(
                "visit_id",
                true,
                false,
                (rs, dto) -> dto.visitId = rs.getObject("visit_id", Integer.class),
                dto -> dto.visitId),
            new Column<WqueueDTO>(
                "wait_state",
                false,
                false,
                (rs, dto) -> dto.waitState = rs.getObject("wait_state", Integer.class),
                dto -> dto.waitState));
  }

  @Override
  protected String getTableName() {
    return "wqueue";
  }

  @Override
  protected Class<WqueueDTO> getClassDTO() {
    return WqueueDTO.class;
  }

  @Override
  protected List<Column<WqueueDTO>> getColumns() {
    return columns;
  }
}
