package jp.chang.myclinic.backendsqlite.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.Query;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import jp.chang.myclinic.backenddb.tableinterface.GazouLabelTableInterface;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.GazouLabelDTO;

public class GazouLabelTableBase extends Table<GazouLabelDTO> implements GazouLabelTableInterface {

  public GazouLabelTableBase(Query query) {
    super(query);
  }

  private static List<Column<GazouLabelDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "conduct_id",
                "conductId",
                true,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.conductId),
                (rs, i, dto) -> dto.conductId = rs.getInt(i)),
            new Column<>(
                "label",
                "label",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.label),
                (rs, i, dto) -> dto.label = rs.getString(i)));
  }

  @Override()
  public String getTableName() {
    return "gazou_label";
  }

  @Override()
  protected Class<GazouLabelDTO> getClassDTO() {
    return GazouLabelDTO.class;
  }

  @Override()
  protected List<Column<GazouLabelDTO>> getColumns() {
    return columns;
  }
}
