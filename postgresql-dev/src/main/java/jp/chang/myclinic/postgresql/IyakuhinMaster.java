package jp.chang.myclinic.postgresql;

import jp.chang.myclinic.dto.IyakuhinMasterDTO;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class IyakuhinMaster {

    //private static Logger logger = LoggerFactory.getLogger(IyakuhinMaster.class);

    static IntegerColumn<IyakuhinMasterDTO> iyakuhincode = new IntegerColumn<>(
            "iyakuhincode",
            (dto, value) -> dto.iyakuhincode = value,
            dto -> dto.iyakuhincode
    );

    static StringColumn<IyakuhinMasterDTO> name = new StringColumn<>(
            "name",
            (dto, value) -> dto.name = value,
            dto -> dto.name
    );

    static StringColumn<IyakuhinMasterDTO> yomi = new StringColumn<>(
            "yomi",
            (dto, value) -> dto.yomi = value,
            dto -> dto.yomi
    );

    static StringColumn<IyakuhinMasterDTO> unit = new StringColumn<>(
            "unit",
            (dto, value) -> dto.unit = value,
            dto -> dto.unit
    );

    static DoubleColumn<IyakuhinMasterDTO> yakka = new DoubleColumn<>(
            "yakka",
            (dto, value) -> dto.yakka = value,
            dto -> dto.yakka
    ){
        @Override
        Double getRowValue(ResultSet resultSet, int index) throws SQLException {
            BigDecimal dValue = resultSet.getBigDecimal(index);
            return dValue.doubleValue();
        }

        @Override
        void setQueryParam(PreparedStatement stmt, int index, Double value) throws SQLException {
            BigDecimal dValue = new BigDecimal(value);
            stmt.setBigDecimal(index, dValue);
        }
    };

    static CharacterColumn<IyakuhinMasterDTO> madoku = new CharacterColumn<>(
            "madoku",
            (dto, value) -> dto.madoku = value,
            dto -> dto.madoku
    );

    static CharacterColumn<IyakuhinMasterDTO> kouhatsu = new CharacterColumn<>(
            "kouhatsu",
            (dto, value) -> dto.kouhatsu = value,
            dto -> dto.kouhatsu
    );

    static CharacterColumn<IyakuhinMasterDTO> zaikei = new CharacterColumn<>(
            "zaikei",
            (dto, value) -> dto.zaikei = value,
            dto -> dto.zaikei
    );

    static StringColumn<IyakuhinMasterDTO> validFrom = new StringColumn<>(
            "valid_from",
            (dto, value) -> dto.validFrom = value,
            dto -> dto.validFrom
    ){
        @Override
        String getRowValue(ResultSet resultSet, int index) throws SQLException {
            LocalDate dValue = resultSet.getObject(index, LocalDate.class);
            return dValue.toString();
        }

        @Override
        void setQueryParam(PreparedStatement stmt, int index, String value) throws SQLException {
            LocalDate dValue = LocalDate.parse(value);
            stmt.setObject(index, dValue);
        }
    };

    static StringColumn<IyakuhinMasterDTO> validUpto = new StringColumn<>(
            "valid_upto",
            (dto, value) -> dto.validUpto = value,
            dto -> dto.validUpto
    ){
        @Override
        String getRowValue(ResultSet resultSet, int index) throws SQLException {
            LocalDate dValue = resultSet.getObject(index, LocalDate.class);
            return dValue == null ? "0000-00-00" : dValue.toString();
        }

        @Override
        void setQueryParam(PreparedStatement stmt, int index, String value) throws SQLException {
            LocalDate dValue;
            if( "0000-00-00".equals(value) || value == null ){
                dValue = null;
            } else {
                dValue = LocalDate.parse(value);
            }
            stmt.setObject(index, dValue);
        }
    };

    private static Columns<IyakuhinMasterDTO> columns = new Columns<>(List.of(
            iyakuhincode, name, yomi, unit, yakka, madoku, kouhatsu, zaikei, validFrom, validUpto
    ));

    public static Columns<IyakuhinMasterDTO> columns(){
        return columns;
    }

}
