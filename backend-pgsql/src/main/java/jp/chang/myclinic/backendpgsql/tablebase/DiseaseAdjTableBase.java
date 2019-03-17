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

public class DiseaseAdjTableBase extends Table<DiseaseAdjDTO> {

  private static List<Column<DiseaseAdjDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "disease_adj_id",
                true,
                true,
                (stmt, i, dto) -> stmt.setInt(i, dto.diseaseAdjId),
                (rs, i, dto) -> dto.diseaseAdjId = rs.getObject(i, Integer.class)),
            new Column<>(
                "disease_id",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.diseaseId),
                (rs, i, dto) -> dto.diseaseId = rs.getObject(i, Integer.class)),
            new Column<>(
                "shuushokugocode",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.shuushokugocode),
                (rs, i, dto) -> dto.shuushokugocode = rs.getObject(i, Integer.class)));
  }

  @Override()
  protected String getTableName() {
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
