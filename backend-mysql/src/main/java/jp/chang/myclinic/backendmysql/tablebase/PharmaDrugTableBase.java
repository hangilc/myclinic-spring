package jp.chang.myclinic.backendmysql.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.Query;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.PharmaDrugDTO;

public class PharmaDrugTableBase extends Table<PharmaDrugDTO> {

  public PharmaDrugTableBase(Query query) {
    super(query);
  }

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
                (rs, i, dto) -> dto.iyakuhincode = rs.getInt(i)),
            new Column<>(
                "description",
                "description",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.description),
                (rs, i, dto) -> dto.description = rs.getString(i)),
            new Column<>(
                "sideeffect",
                "sideeffect",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.sideeffect),
                (rs, i, dto) -> dto.sideeffect = rs.getString(i)));
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
