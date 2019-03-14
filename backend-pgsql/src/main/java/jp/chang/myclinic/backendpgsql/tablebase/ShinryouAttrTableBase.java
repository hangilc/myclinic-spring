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

public class ShinryouAttrTableBase extends Table<ShinryouAttrDTO> {

  private static List<Column<ShinryouAttrDTO>> columns;

  static {
    columns =
        List.of(
            new Column<ShinryouAttrDTO>(
                "shinryou_id",
                true,
                false,
                (rs, dto) -> dto.shinryouId = rs.getObject("shinryou_id", Integer.class),
                dto -> dto.shinryouId),
            new Column<ShinryouAttrDTO>(
                "tekiyou",
                false,
                false,
                (rs, dto) -> dto.tekiyou = rs.getObject("tekiyou", String.class),
                dto -> dto.tekiyou));
  }

  @Override
  protected String getTableName() {
    return "shinryou_attr";
  }

  @Override
  protected Class<ShinryouAttrDTO> getClassDTO() {
    return ShinryouAttrDTO.class;
  }

  @Override
  protected List<Column<ShinryouAttrDTO>> getColumns() {
    return columns;
  }
}
