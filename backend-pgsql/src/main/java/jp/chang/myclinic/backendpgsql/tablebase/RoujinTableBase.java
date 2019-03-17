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

public class RoujinTableBase extends Table<RoujinDTO> {

  private static List<Column<RoujinDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "roujin_id",
                true,
                true,
                (stmt, i, dto) -> stmt.setInt(i, dto.roujinId),
                (rs, i, dto) -> dto.roujinId = rs.getObject(i, Integer.class)),
            new Column<>(
                "patient_id",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.patientId),
                (rs, i, dto) -> dto.patientId = rs.getObject(i, Integer.class)),
            new Column<>(
                "shichouson",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.shichouson),
                (rs, i, dto) -> dto.shichouson = rs.getObject(i, Integer.class)),
            new Column<>(
                "jukyuusha",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.jukyuusha),
                (rs, i, dto) -> dto.jukyuusha = rs.getObject(i, Integer.class)),
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
    return "roujin";
  }

  @Override()
  protected Class<RoujinDTO> getClassDTO() {
    return RoujinDTO.class;
  }

  @Override()
  protected List<Column<RoujinDTO>> getColumns() {
    return columns;
  }
}
