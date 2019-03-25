package jp.chang.myclinic.backendmysql.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.Query;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.ByoumeiMasterDTO;

public class ByoumeiMasterTableBase extends Table<ByoumeiMasterDTO> {

  public ByoumeiMasterTableBase(Query query) {
    super(query);
  }

  private static List<Column<ByoumeiMasterDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "shoubyoumeicode",
                "shoubyoumeicode",
                true,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.shoubyoumeicode),
                (rs, i, dto) -> dto.shoubyoumeicode = rs.getInt(i)),
            new Column<>(
                "name",
                "name",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.name),
                (rs, i, dto) -> dto.name = rs.getString(i)),
            new Column<>(
                "valid_from",
                "validFrom",
                true,
                false,
                (stmt, i, dto) -> stmt.setObject(i, LocalDate.parse(dto.validFrom)),
                (rs, i, dto) -> dto.validFrom = rs.getObject(i, LocalDate.class).toString()),
            new Column<>(
                "valid_upto",
                "validUpto",
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
  public String getTableName() {
    return "shoubyoumei_master_arch";
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
