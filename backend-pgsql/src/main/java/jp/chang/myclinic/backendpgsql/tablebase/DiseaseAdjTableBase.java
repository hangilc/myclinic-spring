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

public class DiseaseAdjTableBase extends Table<DiseaseAdjDTO> {

  private static List<Column<DiseaseAdjDTO>> columns;

  static {
    columns =
        List.of(
            new Column<DiseaseAdjDTO>(
                "disease_adj_id",
                true,
                true,
                (rs, dto) -> dto.diseaseAdjId = rs.getObject("disease_adj_id", Integer.class),
                dto -> dto.diseaseAdjId),
            new Column<DiseaseAdjDTO>(
                "disease_id",
                false,
                false,
                (rs, dto) -> dto.diseaseId = rs.getObject("disease_id", Integer.class),
                dto -> dto.diseaseId),
            new Column<DiseaseAdjDTO>(
                "shuushokugocode",
                false,
                false,
                (rs, dto) -> dto.shuushokugocode = rs.getObject("shuushokugocode", Integer.class),
                dto -> dto.shuushokugocode));
  }

  @Override
  protected String getTableName() {
    return "disease_adj";
  }

  @Override
  protected Class<DiseaseAdjDTO> getClassDTO() {
    return DiseaseAdjDTO.class;
  }

  @Override
  protected List<Column<DiseaseAdjDTO>> getColumns() {
    return columns;
  }
}
