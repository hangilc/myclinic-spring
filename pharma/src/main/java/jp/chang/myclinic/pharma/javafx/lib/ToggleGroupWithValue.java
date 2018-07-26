package jp.chang.myclinic.pharma.javafx.lib;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ToggleGroupWithValue<E> {

    //private static Logger logger = LoggerFactory.getLogger(ToggleGroupWithValue.class);
    private ToggleGroup group = new ToggleGroup();
    private Map<Toggle, E> valueMap = new HashMap<>();
    private ObjectProperty<E> value = new SimpleObjectProperty<>();

    public ToggleGroupWithValue() {
        group.selectedToggleProperty().addListener((obs, oldValue, newValue) -> {
            E v = valueMap.get(newValue);
            value.setValue(v);
        });
    }

    public void addToggle(Toggle toggle, E value){
        valueMap.put(toggle, value);
        group.getToggles().add(toggle);
    }

    public ToggleGroup getGroup() {
        return group;
    }

    public void setGroup(ToggleGroup group) {
        this.group = group;
    }

    public Object getValue() {
        return value.get();
    }

    public ObjectProperty<E> valueProperty() {
        return value;
    }

    public void setValue(E value) {
        for(Toggle t: valueMap.keySet()){
            E v = valueMap.get(t);
            if( Objects.equals(v, value) ){
                t.setSelected(true);
                this.value.set(value);
                return;
            }
        }
        throw new InvalidParameterException("No toggle for the value: " + value);
    }
}
