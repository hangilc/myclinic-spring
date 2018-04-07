package jp.chang.myclinic.pharma.javafx.lib;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

import java.util.*;

public class RadioButtonGroup<T>  {

    private ToggleGroup toggleGroup = new ToggleGroup();
    private Map<Toggle, T> valueMap = new HashMap<>();
    private ObjectProperty<T> value = new SimpleObjectProperty<>();
    private List<RadioButton> buttons = new ArrayList<>();

    public RadioButtonGroup(){
        value.addListener((observable, oldValue, newValue) -> {
            for(Map.Entry<Toggle, T> entry : valueMap.entrySet()){
                if( Objects.equals(entry.getValue(), newValue) ){
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
        buttons.add(radio);
        return radio;
    }

    public T getValue() {
        return value.getValue();
    }

    public ObjectProperty<T> valueProperty() {
        return value;
    }

    public void setValue(T value) {
        this.value.setValue(value);
    }

    public RadioButton[] getButtons(){
        return buttons.toArray(new RadioButton[]{});
    }
}
