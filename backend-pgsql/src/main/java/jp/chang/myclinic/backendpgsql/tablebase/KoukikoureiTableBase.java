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

public class KoukikoureiTableBase extends Table<KoukikoureiDTO> {

  private static List<Column<KoukikoureiDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "koukikourei_id",
                true,
                true,
                (stmt, i, dto) -> stmt.setInt(i, dto.koukikoureiId),
                (rs, i, dto) -> dto.koukikoureiId = rs.getObject(i, Integer.class)),
            new Column<>(
                "patient_id",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.patientId),
                (rs, i, dto) -> dto.patientId = rs.getObject(i, Integer.class)),
            new Column<>(
                "hokensha_bangou",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, Integer.parseInt(dto.hokenshaBangou)),
                (rs, i, dto) -> dto.hokenshaBangou = rs.getObject(i, Integer.class).toString()),
            new Column<>(
                "hihokensha_bangou",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, Integer.parseInt(dto.hihokenshaBangou)),
                (rs, i, dto) -> dto.hihokenshaBangou = rs.getObject(i, Integer.class).toString()),
            new Column<>(
                "futan_wari",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.futanWari),
                (rs, i, dto) -> dto.futanWari = rs.getObject(i, Integer.class)),
            new Column<>(
                "valid_from",
                false,
                false,
                (stmt, i, dto) -> stmt.setObject(i, LocalDate.parse(dto.validFrom)),
                (rs, i, dto) -> dto.validFrom = rs.getObject(i, LocalDate.class).toString()),
            new Column<>(
                "valid_upto",
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
  protected String getTableName() {
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
