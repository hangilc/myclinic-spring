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

public class ChargeTableBase extends Table<ChargeDTO> {

  private static List<Column<ChargeDTO>> columns;

  static {
    columns =
        List.of(
            new Column<ChargeDTO>(
                "visit_id",
                true,
                false,
                (rs, dto) -> dto.visitId = rs.getObject("visit_id", Integer.class),
                dto -> dto.visitId),
            new Column<ChargeDTO>(
                "charge",
                false,
                false,
                (rs, dto) -> dto.charge = rs.getObject("charge", Integer.class),
                dto -> dto.charge));
  }

  @Override
  protected String getTableName() {
    return "charge";
  }

  @Override
  protected Class<ChargeDTO> getClassDTO() {
    return ChargeDTO.class;
  }

  @Override
  protected List<Column<ChargeDTO>> getColumns() {
    return columns;
  }
}
