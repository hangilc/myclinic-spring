package jp.chang.myclinic.backendmysql.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.Query;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import jp.chang.myclinic.backenddb.tableinterface.KoukikoureiTableInterface;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.KoukikoureiDTO;

public class KoukikoureiTableBase extends Table<KoukikoureiDTO>
    implements KoukikoureiTableInterface {

  public KoukikoureiTableBase(Query query) {
    super(query);
  }

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
                (rs, i, dto) -> dto.koukikoureiId = rs.getInt(i)),
            new Column<>(
                "patient_id",
                "patientId",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.patientId),
                (rs, i, dto) -> dto.patientId = rs.getInt(i)),
            new Column<>(
                "hokensha_bangou",
                "hokenshaBangou",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.hokenshaBangou),
                (rs, i, dto) -> dto.hokenshaBangou = rs.getString(i)),
            new Column<>(
                "hihokensha_bangou",
                "hihokenshaBangou",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.hihokenshaBangou),
                (rs, i, dto) -> dto.hihokenshaBangou = rs.getString(i)),
            new Column<>(
                "futan_wari",
                "futanWari",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.futanWari),
                (rs, i, dto) -> dto.futanWari = rs.getInt(i)),
            new Column<>(
                "valid_from",
                "validFrom",
                false,
                false,
                (stmt, i, dto) -> stmt.setObject(i, LocalDate.parse(dto.validFrom)),
                (rs, i, dto) -> dto.validFrom = rs.getObject(i, LocalDate.class).toString()),
            new Column<>(
                "valid_upto",
                "validUpto",
                false,
                false,
                (stmt, i, dto) ->
                    stmt.setObject(
                        i, TableBaseHelper.validUptoFromStringToLocalDate(dto.validUpto)),
                (rs, i, dto) ->
                    dto.validUpto =
                        TableBaseHelper.validUptoFromLocalDateToString(
                            rs.getObject(i, LocalDate.class))));
  }

  @Override()
  public String getTableName() {
    return "hoken_koukikourei";
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
