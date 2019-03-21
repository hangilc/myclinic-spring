package jp.chang.myclinic.backendsqlite.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import jp.chang.myclinic.backenddb.tableinterface.ShahokokuhoTableInterface;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.ShahokokuhoDTO;

public class ShahokokuhoTableBase extends Table<ShahokokuhoDTO>
    implements ShahokokuhoTableInterface {

  private static List<Column<ShahokokuhoDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "shahokokuho_id",
                "shahokokuhoId",
                true,
                true,
                (stmt, i, dto) -> stmt.setInt(i, dto.shahokokuhoId),
                (rs, i, dto) -> dto.shahokokuhoId = rs.getObject(i, Integer.class)),
            new Column<>(
                "patient_id",
                "patientId",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.patientId),
                (rs, i, dto) -> dto.patientId = rs.getObject(i, Integer.class)),
            new Column<>(
                "hokensha_bangou",
                "hokenshaBangou",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.hokenshaBangou),
                (rs, i, dto) -> dto.hokenshaBangou = rs.getObject(i, Integer.class)),
            new Column<>(
                "hihokensha_kigou",
                "hihokenshaKigou",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.hihokenshaKigou),
                (rs, i, dto) -> dto.hihokenshaKigou = rs.getObject(i, String.class)),
            new Column<>(
                "hihokensha_bangou",
                "hihokenshaBangou",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.hihokenshaBangou),
                (rs, i, dto) -> dto.hihokenshaBangou = rs.getObject(i, String.class)),
            new Column<>(
                "honnin",
                "honnin",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.honnin),
                (rs, i, dto) -> dto.honnin = rs.getObject(i, Integer.class)),
            new Column<>(
                "kourei",
                "kourei",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.kourei),
                (rs, i, dto) -> dto.kourei = rs.getObject(i, Integer.class)),
            new Column<>(
                "valid_from",
                "validFrom",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.validFrom),
                (rs, i, dto) -> dto.validFrom = rs.getObject(i, String.class)),
            new Column<>(
                "valid_upto",
                "validUpto",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.validUpto),
                (rs, i, dto) -> dto.validUpto = rs.getObject(i, String.class)));
  }

  @Override()
  public String getTableName() {
    return "shahokokuho";
  }

  @Override()
  protected Class<ShahokokuhoDTO> getClassDTO() {
    return ShahokokuhoDTO.class;
  }

  @Override()
  protected List<Column<ShahokokuhoDTO>> getColumns() {
    return columns;
  }
}
