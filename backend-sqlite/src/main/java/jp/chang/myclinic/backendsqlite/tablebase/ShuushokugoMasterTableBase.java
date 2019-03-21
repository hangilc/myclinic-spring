package jp.chang.myclinic.backendsqlite.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.Query;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import jp.chang.myclinic.backenddb.tableinterface.ShuushokugoMasterTableInterface;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.ShuushokugoMasterDTO;

public class ShuushokugoMasterTableBase extends Table<ShuushokugoMasterDTO>
    implements ShuushokugoMasterTableInterface {

  public ShuushokugoMasterTableBase(Query query) {
    super(query);
  }

  private static List<Column<ShuushokugoMasterDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "shuushokugocode",
                "shuushokugocode",
                true,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.shuushokugocode),
                (rs, i, dto) -> dto.shuushokugocode = rs.getInt(i)),
            new Column<>(
                "name",
                "name",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.name),
                (rs, i, dto) -> dto.name = rs.getString(i)));
  }

  @Override()
  public String getTableName() {
    return "shuushokugo_master";
  }

  @Override()
  protected Class<ShuushokugoMasterDTO> getClassDTO() {
    return ShuushokugoMasterDTO.class;
  }

  @Override()
  protected List<Column<ShuushokugoMasterDTO>> getColumns() {
    return columns;
  }
}
