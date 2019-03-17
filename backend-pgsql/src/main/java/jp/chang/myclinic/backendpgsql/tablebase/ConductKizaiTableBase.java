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

public class ConductKizaiTableBase extends Table<ConductKizaiDTO> {

  private static List<Column<ConductKizaiDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "conduct_kizai_id",
                true,
                true,
                (stmt, i, dto) -> stmt.setInt(i, dto.conductKizaiId),
                (rs, i, dto) -> dto.conductKizaiId = rs.getObject(i, Integer.class)),
            new Column<>(
                "conduct_id",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.conductId),
                (rs, i, dto) -> dto.conductId = rs.getObject(i, Integer.class)),
            new Column<>(
                "kizaicode",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.kizaicode),
                (rs, i, dto) -> dto.kizaicode = rs.getObject(i, Integer.class)),
            new Column<>(
                "amount",
                false,
                false,
                (stmt, i, dto) -> stmt.setBigDecimal(i, new BigDecimal(dto.amount)),
                (rs, i, dto) -> dto.amount = rs.getObject(i, BigDecimal.class).doubleValue()));
  }

  @Override()
  protected String getTableName() {
    return "conduct_kizai";
  }

  @Override()
  protected Class<ConductKizaiDTO> getClassDTO() {
    return ConductKizaiDTO.class;
  }

  @Override()
  protected List<Column<ConductKizaiDTO>> getColumns() {
    return columns;
  }
}
