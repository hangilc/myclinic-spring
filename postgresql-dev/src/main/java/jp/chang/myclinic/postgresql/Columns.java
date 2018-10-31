package jp.chang.myclinic.postgresql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Columns<T> {

    //private static Logger logger = LoggerFactory.getLogger(Columns.class);
    private List<Column<T>> columns = new ArrayList<>();

    Columns(List<Column<T>> columns){
        this.columns.addAll(columns);
    }

    Columns<T> except(Column<T> toBeExcluded){
        return new Columns<>(
                columns.stream().filter(c ->  c != toBeExcluded).collect(Collectors.toList())
        );
    }

    public String names(){
        return columns.stream().map(Column::getName).collect(Collectors.joining(","));
    }

    public String names(String prefix){
        String actualPrefix = prefix + ".";
        return columns.stream().map(c -> actualPrefix + c.getName()).collect(Collectors.joining(","));
    }

    public T toDTO(ResultSet rs, T plain) throws SQLException {
        for(int i=0;i<columns.size();i++){
            columns.get(i).setDTO(rs, i+1, plain);
        }
        return plain;
    }

    public void setParameters(PreparedStatement stmt, T dto) throws SQLException {
        for(int i=0;i<columns.size();i++){
            columns.get(i).setParameter(stmt, i+1, dto);
        }
    }

    private Columns() {

    }

}
