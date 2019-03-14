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

public class ConductDrugTableBase extends Table<ConductDrugDTO> {

  private static List<Column<ConductDrugDTO>> columns;

  static {
    columns =
        List.of(
            new Column<ConductDrugDTO>(
                "conduct_drug_id",
                true,
                true,
                (rs, dto) -> dto.conductDrugId = rs.getObject("conduct_drug_id", Integer.class),
                dto -> dto.conductDrugId),
            new Column<ConductDrugDTO>(
                "conduct_id",
                false,
                false,
                (rs, dto) -> dto.conductId = rs.getObject("conduct_id", Integer.class),
                dto -> dto.conductId),
            new Column<ConductDrugDTO>(
                "iyakuhincode",
                false,
                false,
                (rs, dto) -> dto.iyakuhincode = rs.getObject("iyakuhincode", Integer.class),
                dto -> dto.iyakuhincode),
            new Column<ConductDrugDTO>(
                "amount",
                false,
                false,
                (rs, dto) -> dto.amount = rs.getObject("amount", BigDecimal.class).doubleValue(),
                dto -> dto.amount));
  }

  @Override
  protected String getTableName() {
    return "conduct_drug";
  }

  @Override
  protected Class<ConductDrugDTO> getClassDTO() {
    return ConductDrugDTO.class;
  }

  @Override
  protected List<Column<ConductDrugDTO>> getColumns() {
    return columns;
  }
}
