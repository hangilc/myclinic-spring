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

public class PharmaQueueTableBase extends Table<PharmaQueueDTO> {

  private static List<Column<PharmaQueueDTO>> columns;

  static {
    columns =
        List.of(
            new Column<PharmaQueueDTO>(
                "visit_id",
                true,
                false,
                (rs, dto) -> dto.visitId = rs.getObject("visit_id", Integer.class),
                dto -> dto.visitId),
            new Column<PharmaQueueDTO>(
                "pharma_state",
                false,
                false,
                (rs, dto) -> dto.pharmaState = rs.getObject("pharma_state", Integer.class),
                dto -> dto.pharmaState));
  }

  @Override
  protected String getTableName() {
    return "pharma_queue";
  }

  @Override
  protected Class<PharmaQueueDTO> getClassDTO() {
    return PharmaQueueDTO.class;
  }

  @Override
  protected List<Column<PharmaQueueDTO>> getColumns() {
    return columns;
  }
}
