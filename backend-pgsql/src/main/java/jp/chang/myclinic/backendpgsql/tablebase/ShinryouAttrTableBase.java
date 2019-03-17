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

public class ShinryouAttrTableBase extends Table<ShinryouAttrDTO> {

  private static List<Column<ShinryouAttrDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "shinryou_id",
                true,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.shinryouId),
                (rs, i, dto) -> dto.shinryouId = rs.getObject(i, Integer.class)),
            new Column<>(
                "tekiyou",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.tekiyou),
                (rs, i, dto) -> dto.tekiyou = rs.getObject(i, String.class)));
  }

  @Override()
  protected String getTableName() {
    return "shinryou_attr";
  }

  @Override()
  protected Class<ShinryouAttrDTO> getClassDTO() {
    return ShinryouAttrDTO.class;
  }

  @Override()
  protected List<Column<ShinryouAttrDTO>> getColumns() {
    return columns;
  }
}
