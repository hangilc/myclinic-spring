package jp.chang.myclinic.backendsqlite.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import jp.chang.myclinic.backenddb.tableinterface.ConductDrugTableInterface;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.ConductDrugDTO;

public class ConductDrugTableBase extends Table<ConductDrugDTO>
    implements ConductDrugTableInterface {

  private static List<Column<ConductDrugDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "conduct_drug_id",
                "conductDrugId",
                true,
                true,
                (stmt, i, dto) -> stmt.setInt(i, dto.conductDrugId),
                (rs, i, dto) -> dto.conductDrugId = rs.getObject(i, Integer.class)),
            new Column<>(
                "conduct_id",
                "conductId",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.conductId),
                (rs, i, dto) -> dto.conductId = rs.getObject(i, Integer.class)),
            new Column<>(
                "iyakuhincode",
                "iyakuhincode",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.iyakuhincode),
                (rs, i, dto) -> dto.iyakuhincode = rs.getObject(i, Integer.class)),
            new Column<>(
                "amount",
                "amount",
                false,
                false,
                (stmt, i, dto) -> stmt.setDouble(i, dto.amount),
                (rs, i, dto) -> dto.amount = rs.getObject(i, Double.class)));
  }

  @Override()
  public String getTableName() {
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
