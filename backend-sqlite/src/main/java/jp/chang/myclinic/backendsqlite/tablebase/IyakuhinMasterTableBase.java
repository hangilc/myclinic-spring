package jp.chang.myclinic.backendsqlite.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import jp.chang.myclinic.backenddb.tableinterface.IyakuhinMasterTableInterface;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;

public class IyakuhinMasterTableBase extends Table<IyakuhinMasterDTO>
    implements IyakuhinMasterTableInterface {

  private static List<Column<IyakuhinMasterDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "iyakuhincode",
                "iyakuhincode",
                true,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.iyakuhincode),
                (rs, i, dto) -> dto.iyakuhincode = rs.getObject(i, Integer.class)),
            new Column<>(
                "valid_from",
                "validFrom",
                true,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.validFrom),
                (rs, i, dto) -> dto.validFrom = rs.getObject(i, String.class)),
            new Column<>(
                "name",
                "name",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.name),
                (rs, i, dto) -> dto.name = rs.getObject(i, String.class)),
            new Column<>(
                "yomi",
                "yomi",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.yomi),
                (rs, i, dto) -> dto.yomi = rs.getObject(i, String.class)),
            new Column<>(
                "unit",
                "unit",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.unit),
                (rs, i, dto) -> dto.unit = rs.getObject(i, String.class)),
            new Column<>(
                "yakka",
                "yakka",
                false,
                false,
                (stmt, i, dto) -> stmt.setDouble(i, dto.yakka),
                (rs, i, dto) -> dto.yakka = rs.getObject(i, Double.class)),
            new Column<>(
                "madoku",
                "madoku",
                false,
                false,
                (stmt, i, dto) -> stmt.setObject(i, String.valueOf(dto.madoku)),
                (rs, i, dto) -> dto.madoku = rs.getObject(i, String.class).charAt(0)),
            new Column<>(
                "kouhatsu",
                "kouhatsu",
                false,
                false,
                (stmt, i, dto) -> stmt.setObject(i, String.valueOf(dto.kouhatsu)),
                (rs, i, dto) -> dto.kouhatsu = rs.getObject(i, String.class).charAt(0)),
            new Column<>(
                "zaikei",
                "zaikei",
                false,
                false,
                (stmt, i, dto) -> stmt.setObject(i, String.valueOf(dto.zaikei)),
                (rs, i, dto) -> dto.zaikei = rs.getObject(i, String.class).charAt(0)),
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
    return "iyakuhin_master";
  }

  @Override()
  protected Class<IyakuhinMasterDTO> getClassDTO() {
    return IyakuhinMasterDTO.class;
  }

  @Override()
  protected List<Column<IyakuhinMasterDTO>> getColumns() {
    return columns;
  }
}
