package jp.chang.myclinic.backendmysql.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.Query;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import jp.chang.myclinic.backenddb.tableinterface.ShinryouAttrTableInterface;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.ShinryouAttrDTO;

public class ShinryouAttrTableBase extends Table<ShinryouAttrDTO>
    implements ShinryouAttrTableInterface {

  public ShinryouAttrTableBase(Query query) {
    super(query);
  }

  private static List<Column<ShinryouAttrDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "shinryou_id",
                "shinryouId",
                true,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.shinryouId),
                (rs, i, dto) -> dto.shinryouId = rs.getInt(i)),
            new Column<>(
                "tekiyou",
                "tekiyou",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.tekiyou),
                (rs, i, dto) -> dto.tekiyou = rs.getString(i)));
  }

  @Override()
  public String getTableName() {
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
