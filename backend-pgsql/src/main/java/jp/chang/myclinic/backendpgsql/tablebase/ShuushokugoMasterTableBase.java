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

public class ShuushokugoMasterTableBase extends Table<ShuushokugoMasterDTO> {

  private static List<Column<ShuushokugoMasterDTO>> columns;

  static {
    columns =
        List.of(
            new Column<ShuushokugoMasterDTO>(
                "shuushokugocode",
                true,
                false,
                (rs, dto) -> dto.shuushokugocode = rs.getObject("shuushokugocode", Integer.class),
                dto -> dto.shuushokugocode),
            new Column<ShuushokugoMasterDTO>(
                "name",
                false,
                false,
                (rs, dto) -> dto.name = rs.getObject("name", String.class),
                dto -> dto.name));
  }

  @Override
  protected String getTableName() {
    return "shuushokugo_master";
  }

  @Override
  protected Class<ShuushokugoMasterDTO> getClassDTO() {
    return ShuushokugoMasterDTO.class;
  }

  @Override
  protected List<Column<ShuushokugoMasterDTO>> getColumns() {
    return columns;
  }
}
