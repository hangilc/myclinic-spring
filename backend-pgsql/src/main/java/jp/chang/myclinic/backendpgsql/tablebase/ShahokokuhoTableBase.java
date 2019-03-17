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

public class ShahokokuhoTableBase extends Table<ShahokokuhoDTO> {

  private static List<Column<ShahokokuhoDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "shahokokuho_id",
                true,
                true,
                (stmt, i, dto) -> stmt.setInt(i, dto.shahokokuhoId),
                (rs, i, dto) -> dto.shahokokuhoId = rs.getObject(i, Integer.class)),
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
                (stmt, i, dto) -> stmt.setInt(i, dto.hokenshaBangou),
                (rs, i, dto) -> dto.hokenshaBangou = rs.getObject(i, Integer.class)),
            new Column<>(
                "hihokensha_kigou",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.hihokenshaKigou),
                (rs, i, dto) -> dto.hihokenshaKigou = rs.getObject(i, String.class)),
            new Column<>(
                "hihokensha_bangou",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.hihokenshaBangou),
                (rs, i, dto) -> dto.hihokenshaBangou = rs.getObject(i, String.class)),
            new Column<>(
                "honnin",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.honnin),
                (rs, i, dto) -> dto.honnin = rs.getObject(i, Integer.class)),
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
                            rs.getObject(i, LocalDate.class))),
            new Column<>(
                "kourei",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.kourei),
                (rs, i, dto) -> dto.kourei = rs.getObject(i, Integer.class)));
  }

  @Override()
  protected String getTableName() {
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
