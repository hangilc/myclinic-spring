package jp.chang.myclinic.backendmysql.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.Query;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import jp.chang.myclinic.backenddb.tableinterface.DiseaseTableInterface;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.DiseaseDTO;

public class DiseaseTableBase extends Table<DiseaseDTO> implements DiseaseTableInterface {

  public DiseaseTableBase(Query query) {
    super(query);
  }

  private static List<Column<DiseaseDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "disease_id",
                "diseaseId",
                true,
                true,
                (stmt, i, dto) -> stmt.setInt(i, dto.diseaseId),
                (rs, i, dto) -> dto.diseaseId = rs.getInt(i)),
            new Column<>(
                "patient_id",
                "patientId",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.patientId),
                (rs, i, dto) -> dto.patientId = rs.getInt(i)),
            new Column<>(
                "shoubyoumeicode",
                "shoubyoumeicode",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.shoubyoumeicode),
                (rs, i, dto) -> dto.shoubyoumeicode = rs.getInt(i)),
            new Column<>(
                "start_date",
                "startDate",
                false,
                false,
                (stmt, i, dto) -> stmt.setObject(i, LocalDate.parse(dto.startDate)),
                (rs, i, dto) -> dto.startDate = rs.getObject(i, LocalDate.class).toString()),
            new Column<>(
                "end_date",
                "endDate",
                false,
                false,
                (stmt, i, dto) -> stmt.setObject(i, LocalDate.parse(dto.endDate)),
                (rs, i, dto) -> dto.endDate = rs.getObject(i, LocalDate.class).toString()),
            new Column<>(
                "end_reason",
                "endReason",
                false,
                false,
                (stmt, i, dto) -> stmt.setObject(i, dto.endReason),
                (rs, i, dto) -> dto.endReason = rs.getString(i).charAt(0)));
  }

  @Override()
  public String getTableName() {
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
