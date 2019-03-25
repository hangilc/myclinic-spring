package jp.chang.myclinic.backendsqlite.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.Query;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.VisitDTO;

public class VisitTableBase extends Table<VisitDTO> {

  public VisitTableBase(Query query) {
    super(query);
  }

  private static List<Column<VisitDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "visit_id",
                "visitId",
                true,
                true,
                (stmt, i, dto) -> stmt.setInt(i, dto.visitId),
                (rs, i, dto) -> dto.visitId = rs.getInt(i)),
            new Column<>(
                "patient_id",
                "patientId",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.patientId),
                (rs, i, dto) -> dto.patientId = rs.getInt(i)),
            new Column<>(
                "visited_at",
                "visitedAt",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.visitedAt),
                (rs, i, dto) -> dto.visitedAt = rs.getString(i)),
            new Column<>(
                "shahokokuho_id",
                "shahokokuhoId",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.shahokokuhoId),
                (rs, i, dto) -> dto.shahokokuhoId = rs.getInt(i)),
            new Column<>(
                "koukikourei_id",
                "koukikoureiId",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.koukikoureiId),
                (rs, i, dto) -> dto.koukikoureiId = rs.getInt(i)),
            new Column<>(
                "roujin_id",
                "roujinId",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.roujinId),
                (rs, i, dto) -> dto.roujinId = rs.getInt(i)),
            new Column<>(
                "kouhi1_id",
                "kouhi1Id",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.kouhi1Id),
                (rs, i, dto) -> dto.kouhi1Id = rs.getInt(i)),
            new Column<>(
                "kouhi2_id",
                "kouhi2Id",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.kouhi2Id),
                (rs, i, dto) -> dto.kouhi2Id = rs.getInt(i)),
            new Column<>(
                "kouhi3_id",
                "kouhi3Id",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.kouhi3Id),
                (rs, i, dto) -> dto.kouhi3Id = rs.getInt(i)));
  }

  @Override()
  public String getTableName() {
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
