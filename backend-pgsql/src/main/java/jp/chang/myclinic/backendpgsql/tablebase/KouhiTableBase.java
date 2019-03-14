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

public class KouhiTableBase extends Table<KouhiDTO> {

  private static List<Column<KouhiDTO>> columns;

  static {
    columns =
        List.of(
            new Column<KouhiDTO>(
                "kouhi_id",
                true,
                true,
                (rs, dto) -> dto.kouhiId = rs.getObject("kouhi_id", Integer.class),
                dto -> dto.kouhiId),
            new Column<KouhiDTO>(
                "patient_id",
                false,
                false,
                (rs, dto) -> dto.patientId = rs.getObject("patient_id", Integer.class),
                dto -> dto.patientId),
            new Column<KouhiDTO>(
                "futansha",
                false,
                false,
                (rs, dto) -> dto.futansha = rs.getObject("futansha", Integer.class),
                dto -> dto.futansha),
            new Column<KouhiDTO>(
                "jukyuusha",
                false,
                false,
                (rs, dto) -> dto.jukyuusha = rs.getObject("jukyuusha", Integer.class),
                dto -> dto.jukyuusha),
            new Column<KouhiDTO>(
                "valid_from",
                false,
                false,
                (rs, dto) -> dto.validFrom = rs.getObject("valid_from", LocalDate.class).toString(),
                dto -> dto.validFrom),
            new Column<KouhiDTO>(
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
    return "kouhi";
  }

  @Override
  protected Class<KouhiDTO> getClassDTO() {
    return KouhiDTO.class;
  }

  @Override
  protected List<Column<KouhiDTO>> getColumns() {
    return columns;
  }
}
