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

public class ShoukiTableBase extends Table<ShoukiDTO> {

  private static List<Column<ShoukiDTO>> columns;

  static {
    columns =
        List.of(
            new Column<ShoukiDTO>(
                "visit_id",
                true,
                false,
                (rs, dto) -> dto.visitId = rs.getObject("visit_id", Integer.class),
                dto -> dto.visitId),
            new Column<ShoukiDTO>(
                "shouki",
                false,
                false,
                (rs, dto) -> dto.shouki = rs.getObject("shouki", String.class),
                dto -> dto.shouki));
  }

  @Override
  protected String getTableName() {
    return "shouki";
  }

  @Override
  protected Class<ShoukiDTO> getClassDTO() {
    return ShoukiDTO.class;
  }

  @Override
  protected List<Column<ShoukiDTO>> getColumns() {
    return columns;
  }
}
