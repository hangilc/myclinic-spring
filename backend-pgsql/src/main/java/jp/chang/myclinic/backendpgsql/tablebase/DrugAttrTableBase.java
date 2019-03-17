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

public class DrugAttrTableBase extends Table<DrugAttrDTO> {

  private static List<Column<DrugAttrDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "drug_id",
                true,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.drugId),
                (rs, i, dto) -> dto.drugId = rs.getObject(i, Integer.class)),
            new Column<>(
                "tekiyou",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.tekiyou),
                (rs, i, dto) -> dto.tekiyou = rs.getObject(i, String.class)));
  }

  @Override()
  protected String getTableName() {
    return "drug_attr";
  }

  @Override()
  protected Class<DrugAttrDTO> getClassDTO() {
    return DrugAttrDTO.class;
  }

  @Override()
  protected List<Column<DrugAttrDTO>> getColumns() {
    return columns;
  }
}
