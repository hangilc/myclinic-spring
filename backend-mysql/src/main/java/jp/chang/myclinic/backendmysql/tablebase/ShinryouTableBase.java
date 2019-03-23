package jp.chang.myclinic.backendmysql.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.Query;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import jp.chang.myclinic.backenddb.tableinterface.ShinryouTableInterface;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.ShinryouDTO;

public class ShinryouTableBase extends Table<ShinryouDTO> implements ShinryouTableInterface {

  public ShinryouTableBase(Query query) {
    super(query);
  }

  private static List<Column<ShinryouDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "shinryou_id",
                "shinryouId",
                true,
                true,
                (stmt, i, dto) -> stmt.setInt(i, dto.shinryouId),
                (rs, i, dto) -> dto.shinryouId = rs.getInt(i)),
            new Column<>(
                "visit_id",
                "visitId",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.visitId),
                (rs, i, dto) -> dto.visitId = rs.getInt(i)),
            new Column<>(
                "shinryoucode",
                "shinryoucode",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.shinryoucode),
                (rs, i, dto) -> dto.shinryoucode = rs.getInt(i)));
  }

  @Override()
  public String getTableName() {
    return "visit_shinryou";
  }

  @Override()
  protected Class<ShinryouDTO> getClassDTO() {
    return ShinryouDTO.class;
  }

  @Override()
  protected List<Column<ShinryouDTO>> getColumns() {
    return columns;
  }
}
