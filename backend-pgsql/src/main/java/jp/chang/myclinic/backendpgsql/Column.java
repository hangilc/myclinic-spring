package jp.chang.myclinic.backendpgsql;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class Column<DTO> {

    private final String name;
    private final boolean isPrimary;
    private final boolean isAutoIncrement;
    private final BiConsumer<Object, DTO> putIntoDTO;
    private final Function<DTO, Object> getFromDTO;

    public Column(String name, boolean isPrimary, boolean isAutoIncrement,
                  BiConsumer<Object, DTO> putIntoDTO, Function<DTO, Object> getFromDTO) {
        this.name = name;
        this.isPrimary = isPrimary;
        this.isAutoIncrement = isAutoIncrement;
        this.putIntoDTO = putIntoDTO;
        this.getFromDTO = getFromDTO;
    }

    public Column(String name, BiConsumer<Object, DTO> putIntoDTO, Function<DTO, Object> getFromDTO){
        this(name, false, false, putIntoDTO, getFromDTO);
    }

    public String getName() {
        return name;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public boolean isAutoIncrement() {
        return isAutoIncrement;
    }

    public BiConsumer<Object, DTO> putIntoDTO() {
        return putIntoDTO;
    }

    public Function<DTO, Object> getFromDTO() {
        return getFromDTO;
    }

}
