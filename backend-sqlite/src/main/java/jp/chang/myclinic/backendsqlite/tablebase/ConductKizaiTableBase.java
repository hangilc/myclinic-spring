package jp.chang.myclinic.backendsqlite.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import jp.chang.myclinic.backenddb.tableinterface.ConductKizaiTableInterface;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.ConductKizaiDTO;

public class ConductKizaiTableBase extends Table<ConductKizaiDTO>
    implements ConductKizaiTableInterface {

  private static List<Column<ConductKizaiDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "conduct_kizai_id",
                "conductKizaiId",
                true,
                true,
                (stmt, i, dto) -> stmt.setInt(i, dto.conductKizaiId),
                (rs, i, dto) -> dto.conductKizaiId = rs.getObject(i, Integer.class)),
            new Column<>(
                "conduct_id",
                "conductId",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.conductId),
                (rs, i, dto) -> dto.conductId = rs.getObject(i, Integer.class)),
            new Column<>(
                "kizaicode",
                "kizaicode",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.kizaicode),
                (rs, i, dto) -> dto.kizaicode = rs.getObject(i, Integer.class)),
            new Column<>(
                "amount",
                "amount",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, String.valueOf(dto.amount)),
                (rs, i, dto) -> dto.amount = Double.valueOf(rs.getObject(i, String.class))));
  }

  @Override()
  public String getTableName() {
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
