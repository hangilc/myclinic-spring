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

public class KouhiTableBase extends Table<KouhiDTO> {

  private static List<Column<KouhiDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "kouhi_id",
                true,
                true,
                (stmt, i, dto) -> stmt.setInt(i, dto.kouhiId),
                (rs, i, dto) -> dto.kouhiId = rs.getObject(i, Integer.class)),
            new Column<>(
                "patient_id",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.patientId),
                (rs, i, dto) -> dto.patientId = rs.getObject(i, Integer.class)),
            new Column<>(
                "futansha",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.futansha),
                (rs, i, dto) -> dto.futansha = rs.getObject(i, Integer.class)),
            new Column<>(
                "jukyuusha",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.jukyuusha),
                (rs, i, dto) -> dto.jukyuusha = rs.getObject(i, Integer.class)),
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
