package jp.chang.myclinic.reception.lib;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

import java.util.HashMap;
import java.util.Map;

public class RadioButtonGroup<T>  {

    private ToggleGroup toggleGroup = new ToggleGroup();
    private Map<Toggle, T> valueMap = new HashMap<>();
    private Property<T> value = new SimpleObjectProperty<>();

    public RadioButtonGroup(){
        value.addListener((observable, oldValue, newValue) -> {
            for(Map.Entry<Toggle, T> entry : valueMap.entrySet()){
                if( entry.getValue() == newValue ){
                    entry.getKey().setSelected(true);
                }
            }
        });
    }

    public RadioButton createRadioButton(String label, T radioValue){
        RadioButton radio = new RadioButton(label);
        toggleGroup.getToggles().add(radio);
        if( valueMap.values().contains(radioValue) ){
            throw new RuntimeException("RadioButton already exists");
        }
        valueMap.put(radio, radioValue);
        radio.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if( newValue ){
                value.setValue(valueMap.get(radio));
            }
        });
        return radio;
    }

    public T getValue() {
        return value.getValue();
    }

    public Property<T> valueProperty() {
        return value;
    }

    public void setValue(T value) {
        this.value.setValue(value);
    }

}
