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

public class DiseaseTableBase extends Table<DiseaseDTO> {

  private static List<Column<DiseaseDTO>> columns;

  static {
    columns =
        List.of(
            new Column<DiseaseDTO>(
                "disease_id",
                true,
                true,
                (rs, dto) -> dto.diseaseId = rs.getObject("disease_id", Integer.class),
                dto -> dto.diseaseId),
            new Column<DiseaseDTO>(
                "patient_id",
                false,
                false,
                (rs, dto) -> dto.patientId = rs.getObject("patient_id", Integer.class),
                dto -> dto.patientId),
            new Column<DiseaseDTO>(
                "shoubyoumeicode",
                false,
                false,
                (rs, dto) -> dto.shoubyoumeicode = rs.getObject("shoubyoumeicode", Integer.class),
                dto -> dto.shoubyoumeicode),
            new Column<DiseaseDTO>(
                "start_date",
                false,
                false,
                (rs, dto) -> dto.startDate = rs.getObject("start_date", LocalDate.class).toString(),
                dto -> dto.startDate),
            new Column<DiseaseDTO>(
                "end_date",
                false,
                false,
                (rs, dto) -> dto.endDate = rs.getObject("end_date", LocalDate.class).toString(),
                dto -> dto.endDate),
            new Column<DiseaseDTO>(
                "end_reason",
                false,
                false,
                (rs, dto) -> dto.endReason = rs.getObject("end_reason", String.class).charAt(0),
                dto -> dto.endReason));
  }

  @Override
  protected String getTableName() {
    return "disease";
  }

  @Override
  protected Class<DiseaseDTO> getClassDTO() {
    return DiseaseDTO.class;
  }

  @Override
  protected List<Column<DiseaseDTO>> getColumns() {
    return columns;
  }
}
