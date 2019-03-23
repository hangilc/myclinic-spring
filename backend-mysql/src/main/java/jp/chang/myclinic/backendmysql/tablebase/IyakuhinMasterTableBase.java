package jp.chang.myclinic.backendmysql.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.Query;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import jp.chang.myclinic.backenddb.tableinterface.IyakuhinMasterTableInterface;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;

public class IyakuhinMasterTableBase extends Table<IyakuhinMasterDTO>
    implements IyakuhinMasterTableInterface {

  public IyakuhinMasterTableBase(Query query) {
    super(query);
  }

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
                (rs, i, dto) -> dto.iyakuhincode = rs.getInt(i)),
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
                "yakka",
                "yakka",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, String.valueOf(dto.yakka)),
                (rs, i, dto) -> dto.yakka = Double.parseDouble(rs.getString(i))),
            new Column<>(
                "madoku",
                "madoku",
                false,
                false,
                (stmt, i, dto) -> stmt.setObject(i, dto.madoku),
                (rs, i, dto) -> dto.madoku = rs.getString(i).charAt(0)),
            new Column<>(
                "kouhatsu",
                "kouhatsu",
                false,
                false,
                (stmt, i, dto) -> stmt.setObject(i, dto.kouhatsu),
                (rs, i, dto) -> dto.kouhatsu = rs.getString(i).charAt(0)),
            new Column<>(
                "zaikei",
                "zaikei",
                false,
                false,
                (stmt, i, dto) -> stmt.setObject(i, dto.zaikei),
                (rs, i, dto) -> dto.zaikei = rs.getString(i).charAt(0)),
            new Column<>(
                "valid_from",
                "validFrom",
                true,
                false,
                (stmt, i, dto) -> stmt.setObject(i, LocalDate.parse(dto.validFrom)),
                (rs, i, dto) -> dto.validFrom = rs.getObject(i, LocalDate.class).toString()),
            new Column<>(
                "valid_upto",
                "validUpto",
                false,
                false,
                (stmt, i, dto) ->
                    stmt.setObject(
                        i, TableBaseHelper.validUptoFromStringToLocalDate(dto.validUpto)),
                (rs, i, dto) ->
                    dto.validUpto =
                        TableBaseHelper.validUptoFromLocalDateToString(
                            rs.getObject(i, LocalDate.class))));
  }

  @Override()
  public String getTableName() {
    return "iyakuhin_master_arch";
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
