package jp.chang.myclinic.serverpostgresql.db.helper;


import java.util.List;

public interface Table<T> {

    List<Column<T>> getColumns();

}
