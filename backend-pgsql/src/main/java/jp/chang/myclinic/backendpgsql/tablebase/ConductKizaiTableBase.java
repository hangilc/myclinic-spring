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

public class ConductKizaiTableBase extends Table<ConductKizaiDTO> {

  private static List<Column<ConductKizaiDTO>> columns;

  static {
    columns =
        List.of(
            new Column<ConductKizaiDTO>(
                "conduct_kizai_id",
                true,
                true,
                (rs, dto) -> dto.conductKizaiId = rs.getObject("conduct_kizai_id", Integer.class),
                dto -> dto.conductKizaiId),
            new Column<ConductKizaiDTO>(
                "conduct_id",
                false,
                false,
                (rs, dto) -> dto.conductId = rs.getObject("conduct_id", Integer.class),
                dto -> dto.conductId),
            new Column<ConductKizaiDTO>(
                "kizaicode",
                false,
                false,
                (rs, dto) -> dto.kizaicode = rs.getObject("kizaicode", Integer.class),
                dto -> dto.kizaicode),
            new Column<ConductKizaiDTO>(
                "amount",
                false,
                false,
                (rs, dto) -> dto.amount = rs.getObject("amount", BigDecimal.class).doubleValue(),
                dto -> dto.amount));
  }

  @Override
  protected String getTableName() {
    return "conduct_kizai";
  }

  @Override
  protected Class<ConductKizaiDTO> getClassDTO() {
    return ConductKizaiDTO.class;
  }

  @Override
  protected List<Column<ConductKizaiDTO>> getColumns() {
    return columns;
  }
}
