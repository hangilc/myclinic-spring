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

public class ShuushokugoMasterTableBase extends Table<ShuushokugoMasterDTO> {

  private static List<Column<ShuushokugoMasterDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "shuushokugocode",
                true,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.shuushokugocode),
                (rs, i, dto) -> dto.shuushokugocode = rs.getObject(i, Integer.class)),
            new Column<>(
                "name",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.name),
                (rs, i, dto) -> dto.name = rs.getObject(i, String.class)));
  }

  @Override()
  protected String getTableName() {
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
