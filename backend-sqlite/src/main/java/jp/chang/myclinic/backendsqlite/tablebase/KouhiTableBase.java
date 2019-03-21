package jp.chang.myclinic.backendsqlite.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.Query;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import jp.chang.myclinic.backenddb.tableinterface.KouhiTableInterface;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.KouhiDTO;

public class KouhiTableBase extends Table<KouhiDTO> implements KouhiTableInterface {

  public KouhiTableBase(Query query) {
    super(query);
  }

  private static List<Column<KouhiDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "kouhi_id",
                "kouhiId",
                true,
                true,
                (stmt, i, dto) -> stmt.setInt(i, dto.kouhiId),
                (rs, i, dto) -> dto.kouhiId = rs.getInt(i)),
            new Column<>(
                "patient_id",
                "patientId",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.patientId),
                (rs, i, dto) -> dto.patientId = rs.getInt(i)),
            new Column<>(
                "futansha",
                "futansha",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.futansha),
                (rs, i, dto) -> dto.futansha = rs.getInt(i)),
            new Column<>(
                "jukyuusha",
                "jukyuusha",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.jukyuusha),
                (rs, i, dto) -> dto.jukyuusha = rs.getInt(i)),
            new Column<>(
                "valid_from",
                "validFrom",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.validFrom),
                (rs, i, dto) -> dto.validFrom = rs.getString(i)),
            new Column<>(
                "valid_upto",
                "validUpto",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.validUpto),
                (rs, i, dto) -> dto.validUpto = rs.getString(i)));
  }

  @Override()
  public String getTableName() {
    return "kouhi";
  }

  @Override()
  protected Class<KouhiDTO> getClassDTO() {
    return KouhiDTO.class;
  }

  @Override()
  protected List<Column<KouhiDTO>> getColumns() {
    return columns;
  }
}
