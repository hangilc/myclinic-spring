package jp.chang.myclinic.backendpgsql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class Column<DTO> {

    private String name;
    private boolean isPrimary;
    private boolean isAutoIncrement;
    private BiConsumer<Object, DTO> putIntoDTO;
    private Function<DTO, Object> getFromDTO;

    public Column(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public Column<DTO> isPrimary(Boolean isPrimary){
        this.isPrimary = isPrimary;
        return this;
    }

    public boolean isAutoIncrement() {
        return isAutoIncrement;
    }

    public Column<DTO> isAutoIncrement(Boolean isAutoIncrement){
        this.isAutoIncrement = isAutoIncrement;
        return this;
    }

    public BiConsumer<Object, DTO> putIntoDTO() {
        return putIntoDTO;
    }

    public Column<DTO> putIntoDTO(BiConsumer<Object, DTO> putter){
        this.putIntoDTO = putter;
        return this;
    }

    public Function<DTO, Object> getFromDTO() {
        return getFromDTO;
    }

    public Column<DTO> getFromDTO(Function<DTO, Object> getter){
        this.getFromDTO = getter;
        return this;
    }

}
