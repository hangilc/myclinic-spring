package jp.chang.myclinic.backendsqlite.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.Query;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import jp.chang.myclinic.backenddb.tableinterface.RoujinTableInterface;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.RoujinDTO;

public class RoujinTableBase extends Table<RoujinDTO> implements RoujinTableInterface {

  public RoujinTableBase(Query query) {
    super(query);
  }

  private static List<Column<RoujinDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "roujin_id",
                "roujinId",
                true,
                true,
                (stmt, i, dto) -> stmt.setInt(i, dto.roujinId),
                (rs, i, dto) -> dto.roujinId = rs.getInt(i)),
            new Column<>(
                "patient_id",
                "patientId",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.patientId),
                (rs, i, dto) -> dto.patientId = rs.getInt(i)),
            new Column<>(
                "shichouson",
                "shichouson",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.shichouson),
                (rs, i, dto) -> dto.shichouson = rs.getInt(i)),
            new Column<>(
                "jukyuusha",
                "jukyuusha",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.jukyuusha),
                (rs, i, dto) -> dto.jukyuusha = rs.getInt(i)),
            new Column<>(
                "futan_wari",
                "futanWari",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.futanWari),
                (rs, i, dto) -> dto.futanWari = rs.getInt(i)),
            new Column<>(
                "valid_from",
                "validFrom",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.validFrom),
                (rs, i, dto) -> dto.validFrom = rs.getString(i)),
            new Column<>(
                "valid_upto",
                "validUpto",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.validUpto),
                (rs, i, dto) -> dto.validUpto = rs.getString(i)));
  }

  @Override()
  public String getTableName() {
    return "roujin";
  }

  @Override()
  protected Class<RoujinDTO> getClassDTO() {
    return RoujinDTO.class;
  }

  @Override()
  protected List<Column<RoujinDTO>> getColumns() {
    return columns;
  }
}
