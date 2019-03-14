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

public class ConductTableBase extends Table<ConductDTO> {

  private static List<Column<ConductDTO>> columns;

  static {
    columns =
        List.of(
            new Column<ConductDTO>(
                "conduct_id",
                true,
                true,
                (rs, dto) -> dto.conductId = rs.getObject("conduct_id", Integer.class),
                dto -> dto.conductId),
            new Column<ConductDTO>(
                "visit_id",
                false,
                false,
                (rs, dto) -> dto.visitId = rs.getObject("visit_id", Integer.class),
                dto -> dto.visitId),
            new Column<ConductDTO>(
                "kind",
                false,
                false,
                (rs, dto) -> dto.kind = rs.getObject("kind", Integer.class),
                dto -> dto.kind));
  }

  @Override
  protected String getTableName() {
    return "conduct";
  }

  @Override
  protected Class<ConductDTO> getClassDTO() {
    return ConductDTO.class;
  }

  @Override
  protected List<Column<ConductDTO>> getColumns() {
    return columns;
  }
}
