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

public class VisitTableBase extends Table<VisitDTO> {

  private static List<Column<VisitDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "visit_id",
                true,
                true,
                (stmt, i, dto) -> stmt.setInt(i, dto.visitId),
                (rs, i, dto) -> dto.visitId = rs.getObject(i, Integer.class)),
            new Column<>(
                "patient_id",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.patientId),
                (rs, i, dto) -> dto.patientId = rs.getObject(i, Integer.class)),
            new Column<>(
                "visited_at",
                false,
                false,
                (stmt, i, dto) ->
                    stmt.setObject(i, TableBaseHelper.stringToLocalDateTime(dto.visitedAt)),
                (rs, i, dto) ->
                    dto.visitedAt =
                        TableBaseHelper.localDateTimeToString(
                            rs.getObject(i, LocalDateTime.class))),
            new Column<>(
                "shahokokuho_id",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.shahokokuhoId),
                (rs, i, dto) -> dto.shahokokuhoId = rs.getObject(i, Integer.class)),
            new Column<>(
                "roujin_id",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.roujinId),
                (rs, i, dto) -> dto.roujinId = rs.getObject(i, Integer.class)),
            new Column<>(
                "koukikourei_id",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.koukikoureiId),
                (rs, i, dto) -> dto.koukikoureiId = rs.getObject(i, Integer.class)),
            new Column<>(
                "kouhi_1_id",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.kouhi1Id),
                (rs, i, dto) -> dto.kouhi1Id = rs.getObject(i, Integer.class)),
            new Column<>(
                "kouhi_2_id",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.kouhi2Id),
                (rs, i, dto) -> dto.kouhi2Id = rs.getObject(i, Integer.class)),
            new Column<>(
                "kouhi_3_id",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.kouhi3Id),
                (rs, i, dto) -> dto.kouhi3Id = rs.getObject(i, Integer.class)));
  }

  @Override()
  protected String getTableName() {
    return "visit";
  }

  @Override()
  protected Class<VisitDTO> getClassDTO() {
    return VisitDTO.class;
  }

  @Override()
  protected List<Column<VisitDTO>> getColumns() {
    return columns;
  }
}
