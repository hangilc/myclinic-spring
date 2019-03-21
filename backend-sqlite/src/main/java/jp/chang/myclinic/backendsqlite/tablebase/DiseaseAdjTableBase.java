package jp.chang.myclinic.backendsqlite.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.Query;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import jp.chang.myclinic.backenddb.tableinterface.DiseaseAdjTableInterface;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.DiseaseAdjDTO;

public class DiseaseAdjTableBase extends Table<DiseaseAdjDTO> implements DiseaseAdjTableInterface {

  public DiseaseAdjTableBase(Query query) {
    super(query);
  }

  private static List<Column<DiseaseAdjDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "disease_adj_id",
                "diseaseAdjId",
                true,
                true,
                (stmt, i, dto) -> stmt.setInt(i, dto.diseaseAdjId),
                (rs, i, dto) -> dto.diseaseAdjId = rs.getObject(i, Integer.class)),
            new Column<>(
                "disease_id",
                "diseaseId",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.diseaseId),
                (rs, i, dto) -> dto.diseaseId = rs.getObject(i, Integer.class)),
            new Column<>(
                "shuushokugocode",
                "shuushokugocode",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.shuushokugocode),
                (rs, i, dto) -> dto.shuushokugocode = rs.getObject(i, Integer.class)));
  }

  @Override()
  public String getTableName() {
    return "disease_adj";
  }

  @Override()
  protected Class<DiseaseAdjDTO> getClassDTO() {
    return DiseaseAdjDTO.class;
  }

  @Override()
  protected List<Column<DiseaseAdjDTO>> getColumns() {
    return columns;
  }
}
