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

public class ConductDrugTableBase extends Table<ConductDrugDTO> {

  private static List<Column<ConductDrugDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "conduct_drug_id",
                true,
                true,
                (stmt, i, dto) -> stmt.setInt(i, dto.conductDrugId),
                (rs, i, dto) -> dto.conductDrugId = rs.getObject(i, Integer.class)),
            new Column<>(
                "conduct_id",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.conductId),
                (rs, i, dto) -> dto.conductId = rs.getObject(i, Integer.class)),
            new Column<>(
                "iyakuhincode",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.iyakuhincode),
                (rs, i, dto) -> dto.iyakuhincode = rs.getObject(i, Integer.class)),
            new Column<>(
                "amount",
                false,
                false,
                (stmt, i, dto) -> stmt.setBigDecimal(i, new BigDecimal(dto.amount)),
                (rs, i, dto) -> dto.amount = rs.getObject(i, BigDecimal.class).doubleValue()));
  }

  @Override()
  protected String getTableName() {
    return "conduct_drug";
  }

  @Override()
  protected Class<ConductDrugDTO> getClassDTO() {
    return ConductDrugDTO.class;
  }

  @Override()
  protected List<Column<ConductDrugDTO>> getColumns() {
    return columns;
  }
}
