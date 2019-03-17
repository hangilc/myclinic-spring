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

public class PharmaDrugTableBase extends Table<PharmaDrugDTO> {

  private static List<Column<PharmaDrugDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "iyakuhincode",
                true,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.iyakuhincode),
                (rs, i, dto) -> dto.iyakuhincode = rs.getObject(i, Integer.class)),
            new Column<>(
                "description",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.description),
                (rs, i, dto) -> dto.description = rs.getObject(i, String.class)),
            new Column<>(
                "side_effect",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.sideeffect),
                (rs, i, dto) -> dto.sideeffect = rs.getObject(i, String.class)));
  }

  @Override()
  protected String getTableName() {
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
