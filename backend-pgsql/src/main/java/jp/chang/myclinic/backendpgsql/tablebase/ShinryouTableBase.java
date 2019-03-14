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

public class ShinryouTableBase extends Table<ShinryouDTO> {

  private static List<Column<ShinryouDTO>> columns;

  static {
    columns =
        List.of(
            new Column<ShinryouDTO>(
                "shinryou_id",
                true,
                true,
                (rs, dto) -> dto.shinryouId = rs.getObject("shinryou_id", Integer.class),
                dto -> dto.shinryouId),
            new Column<ShinryouDTO>(
                "visit_id",
                false,
                false,
                (rs, dto) -> dto.visitId = rs.getObject("visit_id", Integer.class),
                dto -> dto.visitId),
            new Column<ShinryouDTO>(
                "shinryoucode",
                false,
                false,
                (rs, dto) -> dto.shinryoucode = rs.getObject("shinryoucode", Integer.class),
                dto -> dto.shinryoucode));
  }

  @Override
  protected String getTableName() {
    return "shinryou";
  }

  @Override
  protected Class<ShinryouDTO> getClassDTO() {
    return ShinryouDTO.class;
  }

  @Override
  protected List<Column<ShinryouDTO>> getColumns() {
    return columns;
  }
}
