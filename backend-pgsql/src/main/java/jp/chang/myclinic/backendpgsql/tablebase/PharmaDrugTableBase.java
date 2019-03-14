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

public class PharmaDrugTableBase extends Table<PharmaDrugDTO> {

  private static List<Column<PharmaDrugDTO>> columns;

  static {
    columns =
        List.of(
            new Column<PharmaDrugDTO>(
                "iyakuhincode",
                true,
                false,
                (rs, dto) -> dto.iyakuhincode = rs.getObject("iyakuhincode", Integer.class),
                dto -> dto.iyakuhincode),
            new Column<PharmaDrugDTO>(
                "description",
                false,
                false,
                (rs, dto) -> dto.description = rs.getObject("description", String.class),
                dto -> dto.description),
            new Column<PharmaDrugDTO>(
                "side_effect",
                false,
                false,
                (rs, dto) -> dto.sideeffect = rs.getObject("side_effect", String.class),
                dto -> dto.sideeffect));
  }

  @Override
  protected String getTableName() {
    return "pharma_drug";
  }

  @Override
  protected Class<PharmaDrugDTO> getClassDTO() {
    return PharmaDrugDTO.class;
  }

  @Override
  protected List<Column<PharmaDrugDTO>> getColumns() {
    return columns;
  }
}
