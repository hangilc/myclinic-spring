package jp.chang.myclinic.backendsqlite.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.Query;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import jp.chang.myclinic.backenddb.tableinterface.KizaiMasterTableInterface;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.KizaiMasterDTO;

public class KizaiMasterTableBase extends Table<KizaiMasterDTO>
    implements KizaiMasterTableInterface {

  public KizaiMasterTableBase(Query query) {
    super(query);
  }

  private static List<Column<KizaiMasterDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "kizaicode",
                "kizaicode",
                true,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.kizaicode),
                (rs, i, dto) -> dto.kizaicode = rs.getInt(i)),
            new Column<>(
                "valid_from",
                "validFrom",
                true,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.validFrom),
                (rs, i, dto) -> dto.validFrom = rs.getString(i)),
            new Column<>(
                "name",
                "name",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.name),
                (rs, i, dto) -> dto.name = rs.getString(i)),
            new Column<>(
                "yomi",
                "yomi",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.yomi),
                (rs, i, dto) -> dto.yomi = rs.getString(i)),
            new Column<>(
                "unit",
                "unit",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.unit),
                (rs, i, dto) -> dto.unit = rs.getString(i)),
            new Column<>(
                "kingaku",
                "kingaku",
                false,
                false,
                (stmt, i, dto) -> stmt.setDouble(i, dto.kingaku),
                (rs, i, dto) -> dto.kingaku = rs.getDouble(i)),
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
    return "kizai_master";
  }

  @Override()
  protected Class<KizaiMasterDTO> getClassDTO() {
    return KizaiMasterDTO.class;
  }

  @Override()
  protected List<Column<KizaiMasterDTO>> getColumns() {
    return columns;
  }
}
