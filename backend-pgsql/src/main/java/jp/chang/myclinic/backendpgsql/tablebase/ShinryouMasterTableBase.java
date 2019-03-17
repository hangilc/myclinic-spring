package jp.chang.myclinic.backendpgsql.tablebase;

import jp.chang.myclinic.backendpgsql.Column;
import jp.chang.myclinic.backendpgsql.Table;
import jp.chang.myclinic.backendpgsql.tablebasehelper.TableBaseHelper;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.logdto.practicelog.*;
import java.sql.Connection;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;

public class ShinryouMasterTableBase extends Table<ShinryouMasterDTO> {

  private static List<Column<ShinryouMasterDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "shinryoucode",
                true,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.shinryoucode),
                (rs, i, dto) -> dto.shinryoucode = rs.getObject(i, Integer.class)),
            new Column<>(
                "name",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.name),
                (rs, i, dto) -> dto.name = rs.getObject(i, String.class)),
            new Column<>(
                "tensuu",
                false,
                false,
                (stmt, i, dto) -> stmt.setBigDecimal(i, new BigDecimal(dto.tensuu)),
                (rs, i, dto) -> dto.tensuu = rs.getObject(i, BigDecimal.class).intValue()),
            new Column<>(
                "tensuu_shikibetsu",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, String.valueOf(dto.tensuuShikibetsu)),
                (rs, i, dto) -> dto.tensuuShikibetsu = rs.getObject(i, String.class).charAt(0)),
            new Column<>(
                "shuukeisaki",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.shuukeisaki),
                (rs, i, dto) -> dto.shuukeisaki = rs.getObject(i, String.class)),
            new Column<>(
                "houkatsukensa",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.houkatsukensa),
                (rs, i, dto) -> dto.houkatsukensa = rs.getObject(i, String.class)),
            new Column<>(
                "kensagroup",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.kensaGroup),
                (rs, i, dto) -> dto.kensaGroup = rs.getObject(i, String.class)),
            new Column<>(
                "valid_from",
                true,
                false,
                (stmt, i, dto) -> stmt.setObject(i, LocalDate.parse(dto.validFrom)),
                (rs, i, dto) -> dto.validFrom = rs.getObject(i, LocalDate.class).toString()),
            new Column<>(
                "valid_upto",
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
  protected String getTableName() {
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
