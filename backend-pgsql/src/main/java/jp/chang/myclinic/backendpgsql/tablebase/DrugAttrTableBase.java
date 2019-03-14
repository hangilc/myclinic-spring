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

public class DrugAttrTableBase extends Table<DrugAttrDTO> {

  private static List<Column<DrugAttrDTO>> columns;

  static {
    columns =
        List.of(
            new Column<DrugAttrDTO>(
                "drug_id",
                true,
                false,
                (rs, dto) -> dto.drugId = rs.getObject("drug_id", Integer.class),
                dto -> dto.drugId),
            new Column<DrugAttrDTO>(
                "tekiyou",
                false,
                false,
                (rs, dto) -> dto.tekiyou = rs.getObject("tekiyou", String.class),
                dto -> dto.tekiyou));
  }

  @Override
  protected String getTableName() {
    return "drug_attr";
  }

  @Override
  protected Class<DrugAttrDTO> getClassDTO() {
    return DrugAttrDTO.class;
  }

  @Override
  protected List<Column<DrugAttrDTO>> getColumns() {
    return columns;
  }
}
