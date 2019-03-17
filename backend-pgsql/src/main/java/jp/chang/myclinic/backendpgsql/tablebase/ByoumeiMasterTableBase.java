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

public class ByoumeiMasterTableBase extends Table<ByoumeiMasterDTO> {

  private static List<Column<ByoumeiMasterDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "shoubyoumeicode",
                true,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.shoubyoumeicode),
                (rs, i, dto) -> dto.shoubyoumeicode = rs.getObject(i, Integer.class)),
            new Column<>(
                "name",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.name),
                (rs, i, dto) -> dto.name = rs.getObject(i, String.class)),
            new Column<>(
                "valid_from",
                true,
                false,
                (stmt, i, dto) -> stmt.setObject(i, LocalDate.parse(dto.validFrom)),
                (rs, i, dto) -> dto.validFrom = rs.getObject(i, LocalDate.class).toString()),
            new Column<>(
                "valid_upto",
                false,
                false,
                (stmt, i, dto) ->
                    stmt.setObject(
                        i, TableBaseHelper.validUptoFromStringToLocalDate(dto.validUpto)),
                (rs, i, dto) ->
                    dto.validUpto =
                        TableBaseHelper.validUptoFromLocalDateToString(
                            rs.getObject(i, LocalDate.class))));
  }

  @Override()
  protected String getTableName() {
    return "byoumei_master";
  }

  @Override()
  protected Class<ByoumeiMasterDTO> getClassDTO() {
    return ByoumeiMasterDTO.class;
  }

  @Override()
  protected List<Column<ByoumeiMasterDTO>> getColumns() {
    return columns;
  }
}
