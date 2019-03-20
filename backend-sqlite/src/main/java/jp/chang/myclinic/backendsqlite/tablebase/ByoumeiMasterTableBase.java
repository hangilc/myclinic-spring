package jp.chang.myclinic.backendsqlite.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import jp.chang.myclinic.backenddb.tableinterface.ByoumeiMasterTableInterface;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.ByoumeiMasterDTO;

public class ByoumeiMasterTableBase extends Table<ByoumeiMasterDTO>
    implements ByoumeiMasterTableInterface {

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
                (rs, i, dto) -> dto.shoubyoumeicode = rs.getObject(i, Integer.class)),
            new Column<>(
                "name",
                "name",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.name),
                (rs, i, dto) -> dto.name = rs.getObject(i, String.class)),
            new Column<>(
                "valid_from",
                "validFrom",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.validFrom),
                (rs, i, dto) -> dto.validFrom = rs.getObject(i, String.class)),
            new Column<>(
                "valid_upto",
                "validUpto",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.validUpto),
                (rs, i, dto) -> dto.validUpto = rs.getObject(i, String.class)));
  }

  @Override()
  public String getTableName() {
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
