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

public class ByoumeiMasterTableBase extends Table<ByoumeiMasterDTO> {

  private static List<Column<ByoumeiMasterDTO>> columns;

  static {
    columns =
        List.of(
            new Column<ByoumeiMasterDTO>(
                "shoubyoumeicode",
                true,
                false,
                (rs, dto) -> dto.shoubyoumeicode = rs.getObject("shoubyoumeicode", Integer.class),
                dto -> dto.shoubyoumeicode),
            new Column<ByoumeiMasterDTO>(
                "name",
                false,
                false,
                (rs, dto) -> dto.name = rs.getObject("name", String.class),
                dto -> dto.name),
            new Column<ByoumeiMasterDTO>(
                "valid_from",
                true,
                false,
                (rs, dto) -> dto.validFrom = rs.getObject("valid_from", LocalDate.class).toString(),
                dto -> dto.validFrom),
            new Column<ByoumeiMasterDTO>(
                "valid_upto",
                false,
                false,
                (rs, dto) ->
                    dto.validUpto =
                        TableBaseHelper.getValidUpto(rs.getObject("valid_upto", LocalDate.class)),
                dto -> dto.validUpto));
  }

  @Override
  protected String getTableName() {
    return "byoumei_master";
  }

  @Override
  protected Class<ByoumeiMasterDTO> getClassDTO() {
    return ByoumeiMasterDTO.class;
  }

  @Override
  protected List<Column<ByoumeiMasterDTO>> getColumns() {
    return columns;
  }
}
