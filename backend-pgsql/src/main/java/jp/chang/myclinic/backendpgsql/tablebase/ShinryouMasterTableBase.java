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

public class ShinryouMasterTableBase extends Table<ShinryouMasterDTO> {

  private static List<Column<ShinryouMasterDTO>> columns;

  static {
    columns =
        List.of(
            new Column<ShinryouMasterDTO>(
                "shinryoucode",
                true,
                false,
                (rs, dto) -> dto.shinryoucode = rs.getObject("shinryoucode", Integer.class),
                dto -> dto.shinryoucode),
            new Column<ShinryouMasterDTO>(
                "name",
                false,
                false,
                (rs, dto) -> dto.name = rs.getObject("name", String.class),
                dto -> dto.name),
            new Column<ShinryouMasterDTO>(
                "tensuu",
                false,
                false,
                (rs, dto) -> dto.tensuu = rs.getObject("tensuu", BigDecimal.class).intValue(),
                dto -> dto.tensuu),
            new Column<ShinryouMasterDTO>(
                "tensuu_shikibetsu",
                false,
                false,
                (rs, dto) ->
                    dto.tensuuShikibetsu =
                        rs.getObject("tensuu_shikibetsu", String.class).charAt(0),
                dto -> dto.tensuuShikibetsu),
            new Column<ShinryouMasterDTO>(
                "shuukeisaki",
                false,
                false,
                (rs, dto) -> dto.shuukeisaki = rs.getObject("shuukeisaki", String.class),
                dto -> dto.shuukeisaki),
            new Column<ShinryouMasterDTO>(
                "houkatsukensa",
                false,
                false,
                (rs, dto) -> dto.houkatsukensa = rs.getObject("houkatsukensa", String.class),
                dto -> dto.houkatsukensa),
            new Column<ShinryouMasterDTO>(
                "kensagroup",
                false,
                false,
                (rs, dto) -> dto.kensaGroup = rs.getObject("kensagroup", String.class),
                dto -> dto.kensaGroup),
            new Column<ShinryouMasterDTO>(
                "valid_from",
                true,
                false,
                (rs, dto) -> dto.validFrom = rs.getObject("valid_from", LocalDate.class).toString(),
                dto -> dto.validFrom),
            new Column<ShinryouMasterDTO>(
                "valid_upto",
                false,
                false,
                (rs, dto) ->
                    dto.validUpto =
                        TableBaseHelper.getValidUpto(rs.getObject("valid_upto", LocalDate.class)),
                dto -> dto.validUpto));
  }

  @Override
  protected String getTableName() {
    return "shinryou_master";
  }

  @Override
  protected Class<ShinryouMasterDTO> getClassDTO() {
    return ShinryouMasterDTO.class;
  }

  @Override
  protected List<Column<ShinryouMasterDTO>> getColumns() {
    return columns;
  }
}
