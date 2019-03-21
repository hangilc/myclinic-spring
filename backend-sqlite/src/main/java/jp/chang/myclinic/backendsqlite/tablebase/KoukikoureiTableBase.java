package jp.chang.myclinic.backendsqlite.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import jp.chang.myclinic.backenddb.tableinterface.KoukikoureiTableInterface;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.KoukikoureiDTO;

public class KoukikoureiTableBase extends Table<KoukikoureiDTO>
    implements KoukikoureiTableInterface {

  private static List<Column<KoukikoureiDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "koukikourei_id",
                "koukikoureiId",
                true,
                true,
                (stmt, i, dto) -> stmt.setInt(i, dto.koukikoureiId),
                (rs, i, dto) -> dto.koukikoureiId = rs.getObject(i, Integer.class)),
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
                (stmt, i, dto) -> stmt.setString(i, dto.hokenshaBangou),
                (rs, i, dto) -> dto.hokenshaBangou = rs.getObject(i, String.class)),
            new Column<>(
                "hihokensha_bangou",
                "hihokenshaBangou",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.hihokenshaBangou),
                (rs, i, dto) -> dto.hihokenshaBangou = rs.getObject(i, String.class)),
            new Column<>(
                "futan_wari",
                "futanWari",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.futanWari),
                (rs, i, dto) -> dto.futanWari = rs.getObject(i, Integer.class)),
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
    return "koukikourei";
  }

  @Override()
  protected Class<KoukikoureiDTO> getClassDTO() {
    return KoukikoureiDTO.class;
  }

  @Override()
  protected List<Column<KoukikoureiDTO>> getColumns() {
    return columns;
  }
}
