package jp.chang.myclinic.backendpgsql.tablebase;

import jp.chang.myclinic.backendpgsql.Column;
import jp.chang.myclinic.backendpgsql.Table;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.logdto.practicelog.*;
import java.sql.Connection;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.backendpgsql.tablebasehelper.TableBaseHelper;

public class VisitTableBase extends Table<VisitDTO> {

  private static List<Column<VisitDTO>> columns;

  static {
    columns =
        List.of(
            new Column<VisitDTO>(
                "visit_id",
                true,
                true,
                (rs, dto) -> dto.visitId = rs.getObject("visit_id", Integer.class),
                dto -> dto.visitId),
            new Column<VisitDTO>(
                "patient_id",
                false,
                false,
                (rs, dto) -> dto.patientId = rs.getObject("patient_id", Integer.class),
                dto -> dto.patientId),
            new Column<VisitDTO>(
                "visited_at",
                false,
                false,
                (rs, dto) ->
                    dto.visitedAt =
                        TableBaseHelper.localDateTimeToString(
                            rs.getObject("visited_at", LocalDateTime.class)),
                dto -> dto.visitedAt),
            new Column<VisitDTO>(
                "shahokokuho_id",
                false,
                false,
                (rs, dto) -> dto.shahokokuhoId = rs.getObject("shahokokuho_id", Integer.class),
                dto -> dto.shahokokuhoId),
            new Column<VisitDTO>(
                "roujin_id",
                false,
                false,
                (rs, dto) -> dto.roujinId = rs.getObject("roujin_id", Integer.class),
                dto -> dto.roujinId),
            new Column<VisitDTO>(
                "koukikourei_id",
                false,
                false,
                (rs, dto) -> dto.koukikoureiId = rs.getObject("koukikourei_id", Integer.class),
                dto -> dto.koukikoureiId),
            new Column<VisitDTO>(
                "kouhi_1_id",
                false,
                false,
                (rs, dto) -> dto.kouhi1Id = rs.getObject("kouhi_1_id", Integer.class),
                dto -> dto.kouhi1Id),
            new Column<VisitDTO>(
                "kouhi_2_id",
                false,
                false,
                (rs, dto) -> dto.kouhi2Id = rs.getObject("kouhi_2_id", Integer.class),
                dto -> dto.kouhi2Id),
            new Column<VisitDTO>(
                "kouhi_3_id",
                false,
                false,
                (rs, dto) -> dto.kouhi3Id = rs.getObject("kouhi_3_id", Integer.class),
                dto -> dto.kouhi3Id));
  }

  @Override
  protected String getTableName() {
    return "visit";
  }

  @Override
  protected Class<VisitDTO> getClassDTO() {
    return VisitDTO.class;
  }

  @Override
  protected List<Column<VisitDTO>> getColumns() {
    return columns;
  }
}
