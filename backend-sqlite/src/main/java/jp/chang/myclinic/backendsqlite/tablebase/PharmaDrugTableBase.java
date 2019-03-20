package jp.chang.myclinic.backendsqlite.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import jp.chang.myclinic.backenddb.tableinterface.PharmaDrugTableInterface;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.PharmaDrugDTO;

public class PharmaDrugTableBase extends Table<PharmaDrugDTO> implements PharmaDrugTableInterface {

  private static List<Column<PharmaDrugDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "iyakuhincode",
                "iyakuhincode",
                true,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.iyakuhincode),
                (rs, i, dto) -> dto.iyakuhincode = rs.getObject(i, Integer.class)),
            new Column<>(
                "description",
                "description",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.description),
                (rs, i, dto) -> dto.description = rs.getObject(i, String.class)),
            new Column<>(
                "sideeffect",
                "sideeffect",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.sideeffect),
                (rs, i, dto) -> dto.sideeffect = rs.getObject(i, String.class)));
  }

  @Override()
  public String getTableName() {
    return "pharma_drug";
  }

  @Override()
  protected Class<PharmaDrugDTO> getClassDTO() {
    return PharmaDrugDTO.class;
  }

  @Override()
  protected List<Column<PharmaDrugDTO>> getColumns() {
    return columns;
  }
}
