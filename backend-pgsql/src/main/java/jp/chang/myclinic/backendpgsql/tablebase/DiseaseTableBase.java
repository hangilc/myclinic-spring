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

public class DiseaseTableBase extends Table<DiseaseDTO> {

  private static List<Column<DiseaseDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "disease_id",
                true,
                true,
                (stmt, i, dto) -> stmt.setInt(i, dto.diseaseId),
                (rs, i, dto) -> dto.diseaseId = rs.getObject(i, Integer.class)),
            new Column<>(
                "patient_id",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.patientId),
                (rs, i, dto) -> dto.patientId = rs.getObject(i, Integer.class)),
            new Column<>(
                "shoubyoumeicode",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.shoubyoumeicode),
                (rs, i, dto) -> dto.shoubyoumeicode = rs.getObject(i, Integer.class)),
            new Column<>(
                "start_date",
                false,
                false,
                (stmt, i, dto) -> stmt.setObject(i, LocalDate.parse(dto.startDate)),
                (rs, i, dto) -> dto.startDate = rs.getObject(i, LocalDate.class).toString()),
            new Column<>(
                "end_date",
                false,
                false,
                (stmt, i, dto) -> stmt.setObject(i, LocalDate.parse(dto.endDate)),
                (rs, i, dto) -> dto.endDate = rs.getObject(i, LocalDate.class).toString()),
            new Column<>(
                "end_reason",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, String.valueOf(dto.endReason)),
                (rs, i, dto) -> dto.endReason = rs.getObject(i, String.class).charAt(0)));
  }

  @Override()
  protected String getTableName() {
    return "disease";
  }

  @Override()
  protected Class<DiseaseDTO> getClassDTO() {
    return DiseaseDTO.class;
  }

  @Override()
  protected List<Column<DiseaseDTO>> getColumns() {
    return columns;
  }
}
