package jp.chang.myclinic.backendsqlite.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import jp.chang.myclinic.backenddb.tableinterface.ShinryouMasterTableInterface;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.ShinryouMasterDTO;

public class ShinryouMasterTableBase extends Table<ShinryouMasterDTO>
    implements ShinryouMasterTableInterface {

  private static List<Column<ShinryouMasterDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "shinryoucode",
                "shinryoucode",
                true,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.shinryoucode),
                (rs, i, dto) -> dto.shinryoucode = rs.getObject(i, Integer.class)),
            new Column<>(
                "name",
                "name",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.name),
                (rs, i, dto) -> dto.name = rs.getObject(i, String.class)),
            new Column<>(
                "tensuu",
                "tensuu",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, String.valueOf(dto.tensuu)),
                (rs, i, dto) ->
                    dto.tensuu = TableBaseHelper.stringToInteger(rs.getObject(i, String.class))),
            new Column<>(
                "tensuu_shikibetsu",
                "tensuuShikibetsu",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, String.valueOf(dto.tensuuShikibetsu)),
                (rs, i, dto) -> dto.tensuuShikibetsu = rs.getObject(i, String.class).charAt(0)),
            new Column<>(
                "shuukeisaki",
                "shuukeisaki",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.shuukeisaki),
                (rs, i, dto) -> dto.shuukeisaki = rs.getObject(i, String.class)),
            new Column<>(
                "houkatsukensa",
                "houkatsukensa",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.houkatsukensa),
                (rs, i, dto) -> dto.houkatsukensa = rs.getObject(i, String.class)),
            new Column<>(
                "kensa_group",
                "kensaGroup",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.kensaGroup),
                (rs, i, dto) -> dto.kensaGroup = rs.getObject(i, String.class)),
            new Column<>(
                "valid_from",
                "validFrom",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.validFrom),
                (rs, i, dto) -> dto.validFrom = rs.getObject(i, String.class)),
            new Column<>(
                "valid_upto",
                "validUpto",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.validUpto),
                (rs, i, dto) -> dto.validUpto = rs.getObject(i, String.class)));
  }

  @Override()
  public String getTableName() {
    return "shinryou_master";
  }

  @Override()
  protected Class<ShinryouMasterDTO> getClassDTO() {
    return ShinryouMasterDTO.class;
  }

  @Override()
  protected List<Column<ShinryouMasterDTO>> getColumns() {
    return columns;
  }
}
