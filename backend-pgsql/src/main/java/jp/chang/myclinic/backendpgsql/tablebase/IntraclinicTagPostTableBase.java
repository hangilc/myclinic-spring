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

public class IntraclinicTagPostTableBase extends Table<IntraclinicTagPostDTO> {

  private static List<Column<IntraclinicTagPostDTO>> columns;

  static {
    columns =
        List.of(
            new Column<IntraclinicTagPostDTO>(
                "tag_id",
                true,
                false,
                (rs, dto) -> dto.tagId = rs.getObject("tag_id", Integer.class),
                dto -> dto.tagId),
            new Column<IntraclinicTagPostDTO>(
                "post_id",
                true,
                false,
                (rs, dto) -> dto.postId = rs.getObject("post_id", Integer.class),
                dto -> dto.postId));
  }

  @Override
  protected String getTableName() {
    return "intraclinic_tag_post";
  }

  @Override
  protected Class<IntraclinicTagPostDTO> getClassDTO() {
    return IntraclinicTagPostDTO.class;
  }

  @Override
  protected List<Column<IntraclinicTagPostDTO>> getColumns() {
    return columns;
  }
}
