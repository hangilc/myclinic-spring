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

public class DrugTableBase extends Table<DrugDTO> {

  private static List<Column<DrugDTO>> columns;

  static {
    columns =
        List.of(
            new Column<DrugDTO>(
                "drug_id",
                true,
                true,
                (rs, dto) -> dto.drugId = rs.getObject("drug_id", Integer.class),
                dto -> dto.drugId),
            new Column<DrugDTO>(
                "visit_id",
                false,
                false,
                (rs, dto) -> dto.visitId = rs.getObject("visit_id", Integer.class),
                dto -> dto.visitId),
            new Column<DrugDTO>(
                "iyakuhincode",
                false,
                false,
                (rs, dto) -> dto.iyakuhincode = rs.getObject("iyakuhincode", Integer.class),
                dto -> dto.iyakuhincode),
            new Column<DrugDTO>(
                "amount",
                false,
                false,
                (rs, dto) -> dto.amount = rs.getObject("amount", BigDecimal.class).doubleValue(),
                dto -> dto.amount),
            new Column<DrugDTO>(
                "usage",
                false,
                false,
                (rs, dto) -> dto.usage = rs.getObject("usage", String.class),
                dto -> dto.usage),
            new Column<DrugDTO>(
                "days",
                false,
                false,
                (rs, dto) -> dto.days = rs.getObject("days", Integer.class),
                dto -> dto.days),
            new Column<DrugDTO>(
                "category",
                false,
                false,
                (rs, dto) -> dto.category = rs.getObject("category", Integer.class),
                dto -> dto.category),
            new Column<DrugDTO>(
                "prescribed",
                false,
                false,
                (rs, dto) -> dto.prescribed = rs.getObject("prescribed", Integer.class),
                dto -> dto.prescribed));
  }

  @Override
  protected String getTableName() {
    return "drug";
  }

  @Override
  protected Class<DrugDTO> getClassDTO() {
    return DrugDTO.class;
  }

  @Override
  protected List<Column<DrugDTO>> getColumns() {
    return columns;
  }
}
