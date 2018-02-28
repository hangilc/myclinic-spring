package jp.chang.myclinic.practice.javafx.parts;

import javafx.scene.control.ChoiceBox;
import javafx.util.StringConverter;
import jp.chang.myclinic.consts.Sex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SexInput extends ChoiceBox<Sex> {

    private static Logger logger = LoggerFactory.getLogger(SexInput.class);

    public SexInput() {
        this(Sex.Female);
    }

    public SexInput(Sex initialValue){
        getItems().setAll(Sex.Male, Sex.Female);
        setConverter(new StringConverter<Sex>(){
            @Override
            public String toString(Sex object) {
                return object.getKanji();
            }

            @Override
            public Sex fromString(String string) {
                return Sex.fromKanji(string);
            }
        });
        getSelectionModel().select(initialValue);
    }

}
