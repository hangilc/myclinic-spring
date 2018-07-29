package jp.chang.myclinic.practice.javafx;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.ShoukiDTO;

class RecordShouki extends TextFlow {

    //private static Logger logger = LoggerFactory.getLogger(RecordShouki.class);
    private ObjectProperty<ShoukiDTO> shoukiProperty;
    private Text text = new Text();

    RecordShouki(ObjectProperty<ShoukiDTO> shoukiProperty) {
        this.shoukiProperty = shoukiProperty;
        BooleanBinding visibleBinding = Bindings.isNotNull(shoukiProperty);
        visibleProperty().bind(visibleBinding);
        managedProperty().bind(visibleBinding);
        updateText();
        getChildren().add(text);
        shoukiProperty.addListener((obs, oldValue, newValue) -> updateText());
    }

    private void updateText(){
        ShoukiDTO shoukiDTO = shoukiProperty.getValue();
        if( shoukiDTO == null ){
            text.setText("");
        } else {
            text.setText("【症状詳記】" + shoukiDTO.shouki);
        }
    }

}
